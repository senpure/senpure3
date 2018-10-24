package com.senpure.io.server;


import com.senpure.io.message.Server2GatewayMessage;
import com.senpure.io.protocol.Message;
import io.netty.channel.Channel;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GatewayManager {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    private ConcurrentMap<String, GatewayChannelManager> gatewayChannelMap = new ConcurrentHashMap<>();

    private ConcurrentMap<Long, GatewayChannelManager> userGatewayMap = new ConcurrentHashMap<>();

    private ConcurrentMap<Long, GatewayChannelManager> tokenGatewayMap = new ConcurrentHashMap<>();

    public synchronized GatewayChannelManager getGatewayChannelServer(String serverKey) {

        GatewayChannelManager manager = gatewayChannelMap.get(serverKey);
        if (manager == null) {
            manager = new GatewayChannelManager();
            gatewayChannelMap.put(serverKey, manager);
            return gatewayChannelMap.get(serverKey);
        }
        return manager;
    }

    public void relationUser(String serverKey, Long userId) {
        userGatewayMap.putIfAbsent(userId, gatewayChannelMap.get(serverKey));
    }

    public void relationToken(String serverKey, Long token) {
        tokenGatewayMap.putIfAbsent(token, gatewayChannelMap.get(serverKey));
    }

    public void breakUser(Long userId) {
        userGatewayMap.remove(userId);
    }

    public void breakToken(Long token) {
        tokenGatewayMap.remove(token);
    }

    public void offline(long token, long userId) {
        logger.debug("token {} id {}离线", token, userId);
        if (token != 0) {
            breakToken(token);
        }
        if (userId > 0) {
            breakUser(userId);
        }
    }

    public boolean canHandleMessageValue(int messageId, String value) {
        return false;
    }



    public void sendMessage2GatewayByToken(Long token, Message message) {
        Server2GatewayMessage toGateway = new Server2GatewayMessage();
        toGateway.setToken(token);
        toGateway.setUserIds(new Long[0]);
        toGateway.setMessage(message);
        toGateway.setMessageId(message.getMessageId());
        GatewayChannelManager channelServer = tokenGatewayMap.get(token);
        Channel channel = channelServer.nextChannel();
        channel.writeAndFlush(toGateway);
    }

    public void sendMessage2Gateway(Long userId, Message message) {
        Server2GatewayMessage toGateway = new Server2GatewayMessage();
        toGateway.setUserIds(new Long[]{userId});
        toGateway.setMessage(message);
        toGateway.setMessageId(message.getMessageId());
        userGatewayMap.get(userId).nextChannel().writeAndFlush(toGateway);

    }

    public void sendMessage2Gateway(List<Long> userIds, Message message) {
        Map<Integer, GatewayUsers> map = new HashMap<>();
        for (Long userId : userIds) {
            GatewayChannelManager gatewayChannelManager = userGatewayMap.get(userId);
            Integer number = gatewayChannelManager.getNumber();
            GatewayUsers gatewayUsers = map.get(number);
            if (gatewayUsers == null) {
                gatewayUsers = new GatewayUsers();
                gatewayUsers.gatewayChannelManager = gatewayChannelManager;
                map.put(number, gatewayUsers);
            }
            gatewayUsers.userIds.add(userId);
        }
        map.values().forEach(gatewayUsers -> {
            Server2GatewayMessage toGateway = new Server2GatewayMessage();
            toGateway.setMessage(message);
            toGateway.setMessageId(message.getMessageId());
            Long[] users = new Long[gatewayUsers.userIds.size()];
            gatewayUsers.userIds.toArray(users);
            gatewayUsers.gatewayChannelManager.nextChannel().writeAndFlush(toGateway);
        });
    }

    class GatewayUsers {
        List<Long> userIds = new ArrayList<>(16);
        GatewayChannelManager gatewayChannelManager;
    }
}
