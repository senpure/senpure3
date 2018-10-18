package com.senpure.io.gateway;

import com.senpure.io.message.CSRelationPlayerGatewayMessage;
import com.senpure.io.message.CSRelationUserGatewayMessage;
import com.senpure.io.message.Client2GatewayMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 网关管理一个服务的多个实例 每个实例可能含有多个管道channel
 */
public class ServerManager {
    private CSRelationPlayerGatewayMessage message = new CSRelationPlayerGatewayMessage();
    private int relationMessageId = message.getMessageId();
    private ConcurrentMap<Long, Channel> userChannelMap = new ConcurrentHashMap<>();
    private List<ServerChannelManager> useChannelManagers = new ArrayList<>();

    private List<ServerChannelManager> prepStopOldInstance = new ArrayList<>();
    private Map<Integer, Boolean> handleIdsMap = new HashMap<>();
    private String serverName;
    private AtomicInteger atomicIndex = new AtomicInteger(-1);

    public Channel channel(Long userId, Long token) {
        Channel channel = userChannelMap.get(userId);
        if (channel == null) {
            channel =nextServerChannelManager().nextChannel();
            relationGateway(channel, token, userId);
            return channel;
        }
        return channel;
    }

    public void relationGateway(Channel channel, long token, long userId) {

        CSRelationUserGatewayMessage message = new CSRelationUserGatewayMessage();
        message.setToken(token);
        message.setUserId(userId);
        Client2GatewayMessage toMessage = new Client2GatewayMessage();
        toMessage.setMessageId(relationMessageId);
        ByteBuf buf = Unpooled.buffer();
        message.write(buf);
        toMessage.setData(buf.array());
        channel.writeAndFlush(toMessage);
    }

    private ServerChannelManager nextServerChannelManager() {
        ServerChannelManager manager = useChannelManagers.get(nextIndex());
        return manager;
    }

    private int nextIndex() {
        if (useChannelManagers.size() == 0) {
            return 0;
        }
        int index = atomicIndex.incrementAndGet();
       return Math.abs(index % useChannelManagers.size());
//        if (index >= useChannelManagers.size()) {
//            boolean reset = atomicIndex.compareAndSet(index, 0);
//            if (!reset) {
//                return nextIndex();
//            }
//            return 0;
//        }
//        return index;
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
        ServerChannelManager manager = new  ServerChannelManager();
     manager.setServerKey(serverKey);
        return manager;

    }

    public List<ServerChannelManager> getUseChannelManagers() {
        return useChannelManagers;
    }

    public synchronized void checkChannelServer(String serverKey, ServerChannelManager channelServer) {
        for (ServerChannelManager manager : useChannelManagers) {
            if (manager.getServerKey().equals(serverKey)) {
                return;
            }
        }
        useChannelManagers.add(channelServer);
    }

    public void markHandleId(int messageId) {
        handleIdsMap.put(messageId, true);
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
