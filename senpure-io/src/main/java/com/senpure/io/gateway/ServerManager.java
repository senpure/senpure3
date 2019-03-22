package com.senpure.io.gateway;


import com.senpure.io.protocol.Constant;
import com.senpure.io.message.CSBreakUserGatewayMessage;
import com.senpure.io.message.CSRelationUserGatewayMessage;
import com.senpure.io.message.Client2GatewayMessage;
import com.senpure.io.message.SCInnerErrorMessage;
import com.senpure.io.support.MessageIdReader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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

    private ConcurrentMap<Long, ServerRelation> tokenServerChannelManagerMap = new ConcurrentHashMap<>();

    private List<ServerChannelManager> useChannelManagers = new ArrayList<>();

    private List<ServerChannelManager> prepStopOldInstance = new ArrayList<>();
    private Map<Integer, Boolean> handleIdsMap = new HashMap<>();
    private String serverName;
    private AtomicInteger atomicIndex = new AtomicInteger(-1);


    public void bind(Long token, Long relationToken, ServerChannelManager serverChannelManager) {
        ServerRelation serverRelation = new ServerRelation();
        serverRelation.serverChannelManager = serverChannelManager;
        serverRelation.relationToken = relationToken;
        tokenServerChannelManagerMap.put(token, serverRelation);

    }


    public void sendMessage(Client2GatewayMessage client2GatewayMessage) {
        ServerRelation serverRelation = tokenServerChannelManagerMap.get(client2GatewayMessage.getToken());
        ServerChannelManager serverChannelManager;
        if (serverRelation == null) {
            serverChannelManager = nextServerChannelManager();
            if (serverChannelManager == null) {
                logger.warn("{}没有服务实例可以使用", serverName);
                SCInnerErrorMessage errorMessage = new SCInnerErrorMessage();
                errorMessage.setType(Constant.ERROR_NOT_FOUND_SERVER);
                errorMessage.setId(client2GatewayMessage.getMessageId());
                errorMessage.setMessage("没有服务器处理" + MessageIdReader.read(client2GatewayMessage.getMessageId()));
                messageExecuter.sendMessage2Client(errorMessage, client2GatewayMessage.getToken());
            } else {
                relationAndWaitSendMessage(serverChannelManager, client2GatewayMessage);
            }

        } else {
            serverRelation.serverChannelManager.sendMessage(client2GatewayMessage);

        }
    }

    public void relationAndWaitSendMessage(ServerChannelManager serverChannelManager, Client2GatewayMessage client2GatewayMessage) {
        Long relationToken = messageExecuter.idGenerator.nextId();
        CSRelationUserGatewayMessage message = new CSRelationUserGatewayMessage();
        message.setToken(client2GatewayMessage.getToken());
        message.setUserId(client2GatewayMessage.getUserId());
        message.setRelationToken(relationToken);
        Client2GatewayMessage toMessage = new Client2GatewayMessage();
        toMessage.setMessageId(csRelationMessageId);
        ByteBuf buf = Unpooled.buffer(message.getSerializedSize());
        message.write(buf);
        byte[] data = new byte[message.getSerializedSize()];
        buf.readBytes(data);
        toMessage.setData(data);
        WaitRelationTask waitRelationTask = new WaitRelationTask();
        waitRelationTask.setRelationToken(relationToken);
        waitRelationTask.setMessage(client2GatewayMessage);
        waitRelationTask.setServerChannelManager(serverChannelManager);
        waitRelationTask.setServerManager(this);
        messageExecuter.waitRelationMap.put(relationToken, waitRelationTask);
        serverChannelManager.sendMessage(toMessage);
    }

    protected ServerChannelManager nextServerChannelManager() {
        if (useChannelManagers.size() == 0) {
            return null;
        }
        return useChannelManagers.get(nextIndex());
    }

    private int nextIndex() {
        if (useChannelManagers.size() == 1) {
            return 0;
        }
        int index = atomicIndex.incrementAndGet();
        return Math.abs(index % useChannelManagers.size());
    }

    public synchronized void prepStopOldInstance() {
        prepStopOldInstance.addAll(useChannelManagers);
        useChannelManagers.clear();
    }

    /**
     * 服务器掉线
     *
     * @param channel
     */
    public synchronized void serverOffLine(Channel channel) {
        serverOffLine(channel, prepStopOldInstance);
        serverOffLine(channel, useChannelManagers);
    }

    private void serverOffLine(Channel channel, List<ServerChannelManager> channelManagers) {
        Iterator<ServerChannelManager> iterator = channelManagers.iterator();
        while (iterator.hasNext()) {
            ServerChannelManager serverChannelManager = iterator.next();
            if (serverChannelManager.offline(channel)) {
                if (!serverChannelManager.isActive()) {
                    iterator.remove();
                    clearRelation(serverChannelManager);
                }
            }
        }
//        for (int i = 0; i < channelManagers.size(); i++) {
////            ServerChannelManager serverChannelManager = channelManagers.get(i);
////            if (serverChannelManager.offline(channel)) {
////                if (!serverChannelManager.isActive()) {
////                    channelManagers.remove(i);
////                    clearRelation(serverChannelManager);
////                }
////            }
////        }

    }


    public void breakUserGateway(Channel clientChannel, Long token, Long userId, String type) {
        breakUserGateway(clientChannel, token, userId, type, true);
    }

    public void breakUserGateway(Channel clientChannel, Long token, Long userId, String type, boolean localRemove) {
        ServerRelation serverRelation = localRemove ? tokenServerChannelManagerMap.remove(token) : tokenServerChannelManagerMap.get(token);
        if (serverRelation != null) {
            logger.info("{} {} 取消 对{} :token{} userId:{}的 关联  {}",
                    serverName, serverRelation.serverChannelManager.getServerKey(), clientChannel, token, userId, localRemove ? "移除" : "不移除");
            CSBreakUserGatewayMessage breakUserGatewayMessage = new CSBreakUserGatewayMessage();
            breakUserGatewayMessage.setRelationToken(serverRelation.relationToken);
            breakUserGatewayMessage.setUserId(userId);
            breakUserGatewayMessage.setToken(token);
            breakUserGatewayMessage.setType(type);
            Client2GatewayMessage client2GatewayMessage = new Client2GatewayMessage();
            client2GatewayMessage.setMessageId(breakUserGatewayMessage.getMessageId());
            client2GatewayMessage.setUserId(breakUserGatewayMessage.getUserId());
            client2GatewayMessage.setToken(breakUserGatewayMessage.getToken());
            ByteBuf buf = Unpooled.buffer(breakUserGatewayMessage.getSerializedSize());
            breakUserGatewayMessage.write(buf);
            byte[] data = new byte[breakUserGatewayMessage.getSerializedSize()];
            buf.readBytes(data);
            client2GatewayMessage.setData(data);
            serverRelation.serverChannelManager.sendMessage(client2GatewayMessage);
        } else {
            logger.info("{} {} 没有对{} 没有关联 :token{} userId:{} ",
                    serverName, serverRelation.serverChannelManager.getServerKey(), clientChannel, token, userId);

        }
    }


    private void clearRelation(ServerChannelManager serverChannelManager) {
        logger.warn("{} {} 全部channel已经下线 清空关联列表", serverName, serverChannelManager.getServerKey());
        List<Long> tokens = new ArrayList<>();
        for (Map.Entry<Long, ServerRelation> entry : tokenServerChannelManagerMap.entrySet()) {
            if (serverChannelManager == entry.getValue().serverChannelManager) {
                tokens.add(entry.getKey());
            }
        }
        for (Long token : tokens) {
            logger.info("{} 取消关联 {} {} ", token, serverName, serverChannelManager.getServerKey());
            tokenServerChannelManagerMap.remove(token);
        }
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

    class ServerRelation {
        ServerChannelManager serverChannelManager;
        Long relationToken;
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
