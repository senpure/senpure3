package com.senpure.io.gateway;


import com.senpure.io.message.CSRelationUserGatewayMessage;
import com.senpure.io.message.Client2GatewayMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 网关管理一个服务的多个实例 每个实例可能含有多个管道channel
 * 一个服务对应一个 serverManager
 */
public class ServerManager {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private GatewayMessageExecuter messageExecuter;

    public ServerManager(GatewayMessageExecuter messageExecuter) {
        this.messageExecuter = messageExecuter;
    }


    private int csRelationMessageId = new CSRelationUserGatewayMessage().getMessageId();

    private ConcurrentMap<Long, ServerChannelManager> userServerChannelManagerMap = new ConcurrentHashMap<>();

    private List<ServerChannelManager> useChannelManagers = new ArrayList<>();

    private List<ServerChannelManager> prepStopOldInstance = new ArrayList<>();
    private Map<Integer, Boolean> handleIdsMap = new HashMap<>();
    private String serverName;
    private AtomicInteger atomicIndex = new AtomicInteger(-1);
    private int relationWaitTime = 200;
    private int waitTimeFailCount = 0;

    //不用等待返回
    public Channel channel2(Long token, Long userId) {
        Channel channel = null;
        ServerChannelManager serverChannelManager = userServerChannelManagerMap.get(userId);
        if (serverChannelManager == null) {
            serverChannelManager = nextServerChannelManager();
            channel = serverChannelManager.nextChannel();
            Long relationToken = messageExecuter.idGenerator.nextId();
            CSRelationUserGatewayMessage message = new CSRelationUserGatewayMessage();
            message.setToken(token);
            message.setUserId(userId);
            message.setRelationToken(relationToken);
            Client2GatewayMessage toMessage = new Client2GatewayMessage();
            toMessage.setMessageId(csRelationMessageId);
            ByteBuf buf = Unpooled.buffer();
            message.write(buf);
            toMessage.setData(buf.array());
            channel.writeAndFlush(toMessage);
            userServerChannelManagerMap.put(userId, serverChannelManager);
            return channel;
        }
        channel = serverChannelManager.nextChannel();
        return channel;
    }

    public void bind(Long userId, ServerChannelManager serverChannelManager) {
        userServerChannelManagerMap.put(userId, serverChannelManager);

    }

    public Channel channel(Long token, Long userId) {
        Channel channel = null;
        ServerChannelManager serverChannelManager = userServerChannelManagerMap.get(userId);
        if (serverChannelManager == null) {
            serverChannelManager = nextServerChannelManager();
            channel = serverChannelManager.nextChannel();
            Long relationToken = messageExecuter.idGenerator.nextId();
            CSRelationUserGatewayMessage message = new CSRelationUserGatewayMessage();
            message.setToken(token);
            message.setUserId(userId);
            message.setRelationToken(relationToken);

            CountDownLatch countDownLatch = new CountDownLatch(1);
            //messageExecuter.waitRelationMap.put(relationToken, countDownLatch);
            Client2GatewayMessage toMessage = new Client2GatewayMessage();
            toMessage.setMessageId(csRelationMessageId);
            ByteBuf buf = Unpooled.buffer();
            message.write(buf);
            toMessage.setData(buf.array());
            channel.writeAndFlush(toMessage);
            try {
                boolean result = countDownLatch.await(relationWaitTime, TimeUnit.MILLISECONDS);
                if (!result) {
                    waitTimeFailCount++;
                    if (waitTimeFailCount >= 10) {
                        relationWaitTime += 10;
                        waitTimeFailCount = 0;
                    }
                    logger.warn("等待真实服务器返回关联结果过长 relationWaitTime {}  waitTimeFailCount {}", relationWaitTime, waitTimeFailCount);
                    messageExecuter.waitRelationMap.remove(relationToken);
                    return null;
                }
                userServerChannelManagerMap.put(userId, serverChannelManager);
                return channel;
            } catch (InterruptedException e) {
                logger.error("等待关联用户与网关出错", e);
            }

        }
        channel = serverChannelManager.nextChannel();
        return channel;
    }


    public void sendMessage(Client2GatewayMessage client2GatewayMessage) {
        ServerChannelManager serverChannelManager = userServerChannelManagerMap.get(client2GatewayMessage.getUserId());
        if (serverChannelManager == null) {
            serverChannelManager = nextServerChannelManager();
            bindAndSendMessage(serverChannelManager, client2GatewayMessage);

        } else {
            serverChannelManager.sendMessage(client2GatewayMessage);
        }
    }

    public void bindAndSendMessage(ServerChannelManager serverChannelManager , Client2GatewayMessage client2GatewayMessage) {
        Long relationToken = messageExecuter.idGenerator.nextId();
        CSRelationUserGatewayMessage message = new CSRelationUserGatewayMessage();
        message.setToken(client2GatewayMessage.getToken());
        message.setUserId(client2GatewayMessage.getUserId());
        message.setRelationToken(relationToken);
        Client2GatewayMessage toMessage = new Client2GatewayMessage();
        toMessage.setMessageId(csRelationMessageId);
        ByteBuf buf = Unpooled.buffer();
        message.write(buf);
        toMessage.setData(buf.array());
        WaitRelationTask waitRelationTask = new WaitRelationTask();
        waitRelationTask.setRelationToken(relationToken);
        waitRelationTask.setMessage(client2GatewayMessage);
        waitRelationTask.setServerChannelManager(serverChannelManager);
        waitRelationTask.setServerManager(this);
        messageExecuter.waitRelationMap.put(relationToken, waitRelationTask);
        serverChannelManager.sendMessage(toMessage);
    }

    protected ServerChannelManager nextServerChannelManager() {
        ServerChannelManager manager = useChannelManagers.get(nextIndex());
        return manager;
    }

    private int nextIndex() {
        if (useChannelManagers.size() == 0) {
            return 0;
        }
        int index = atomicIndex.incrementAndGet();
        return Math.abs(index % useChannelManagers.size());
    }

    public synchronized void prepStopOldInstance() {
        prepStopOldInstance.addAll(useChannelManagers);
        useChannelManagers.clear();
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    public ServerChannelManager getChannelServer(String serverKey) {
        for (ServerChannelManager manager : useChannelManagers) {
            if (manager.getServerKey().equals(serverKey)) {
                return manager;
            }
        }
        ServerChannelManager manager = new ServerChannelManager();
        manager.setServerKey(serverKey);
        return manager;

    }

    public List<ServerChannelManager> getUseChannelManagers() {
        return useChannelManagers;
    }

    public synchronized void checkChannelServer(String serverKey, ServerChannelManager channelManager) {
        for (ServerChannelManager manager : useChannelManagers) {
            if (manager.getServerKey().equals(serverKey)) {
                return;
            }
        }
        useChannelManagers.add(channelManager);
    }

    public void markHandleId(int messageId) {
        handleIdsMap.put(messageId, true);
    }

    public List<Integer> getHandleIds() {
        return new ArrayList<>(handleIdsMap.keySet());
    }


    public boolean handleId(int messageId) {
        return handleIdsMap.get(messageId) != null;
    }

    public static void main(String[] args) {

        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();

        Integer key = 1;
        String s = map.putIfAbsent(key, "2");
        System.out.println(map.get(key));
        System.out.println(s);
        s = map.putIfAbsent(key, "3");
        System.out.println(map.get(key));
        System.out.println(s);
    }
}
