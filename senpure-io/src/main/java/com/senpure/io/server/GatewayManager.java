package com.senpure.io.server;


import com.senpure.io.message.SCBreakUserGatewayMessage;
import com.senpure.io.message.Server2GatewayMessage;
import com.senpure.io.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GatewayManager {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private ConcurrentMap<String, GatewayChannelManager> gatewayChannelMap = new ConcurrentHashMap<>();

    private ConcurrentMap<Long, GatewayRelation> userGatewayMap = new ConcurrentHashMap<>();

    private ConcurrentMap<Long, GatewayRelation> tokenGatewayMap = new ConcurrentHashMap<>();

    public String getGatewayKey(String host, int port) {
        return host + ":" + port;
    }

    public synchronized GatewayChannelManager getGatewayChannelServer(String gatewayKey) {

        GatewayChannelManager manager = gatewayChannelMap.get(gatewayKey);
        if (manager == null) {
            manager = new GatewayChannelManager(gatewayKey);
            gatewayChannelMap.put(gatewayKey, manager);
            return gatewayChannelMap.get(gatewayKey);
        }
        return manager;
    }

    public void report() {
        for (Map.Entry<String, GatewayChannelManager> entry : gatewayChannelMap.entrySet()) {
            logger.debug("{} {}", entry.getKey());
        }
    }

    /**
     * 将userId与网关关联起来
     *
     * @param gatewayKey
     * @param userId
     * @param relationToken
     */
    public void relationUser(String gatewayKey, Long userId, long relationToken) {
        GatewayChannelManager channelManager = gatewayChannelMap.get(gatewayKey);
        if (channelManager != null) {
            GatewayRelation relation = new GatewayRelation();
            relation.relationToken = relationToken;
            relation.gatewayChannelManager = channelManager;
            userGatewayMap.put(userId, relation);
        }

    }

    /**
     * 将token与网关关联起来
     *
     * @param gatewayKey
     * @param token
     * @param relationToken
     */
    public void relationToken(String gatewayKey, Long token, long relationToken) {
        GatewayChannelManager channelManager = gatewayChannelMap.get(gatewayKey);
        if (channelManager != null) {
            GatewayRelation relation = new GatewayRelation();
            relation.relationToken = relationToken;
            relation.gatewayChannelManager = channelManager;
            tokenGatewayMap.put(token, relation);
        }
    }

    public boolean breakUser(Long userId, long relationToken) {
        GatewayRelation relation = userGatewayMap.get(userId);
        if (relation != null) {
            if (relation.relationToken == relationToken) {
                logger.debug("{} 取消关联user {}", relation.gatewayChannelManager.getGatewayKey(), userId);
                userGatewayMap.remove(userId);
                return true;
            }
        }
        return false;
    }

    public boolean breakToken(Long token, long relationToken) {
        GatewayRelation relation = tokenGatewayMap.get(token);
        if (relation != null) {
            if (relation.relationToken == relationToken) {
                logger.debug("{} 取消关联token {}", relation.gatewayChannelManager.getGatewayKey(), token);
                tokenGatewayMap.remove(token);
                return true;
            }
        }
        return false;
    }


    /**
     * 将消息发送给所有的网关
     *
     * @param message
     */
    public void dispatchMessage2Gateway(Server2GatewayMessage message) {
        for (GatewayChannelManager value : gatewayChannelMap.values()) {
            value.sendMessage(message);
        }

    }

    /**
     * 用户登录成功后调用该方法
     * @param token
     * @param userId
     * @param message
     */
    public void sendLoginSuccessMessage2Gateway(Long token, Long userId, Message message) {
        if (userId == 0) {
            return;
        }
        Server2GatewayMessage toGateway = new Server2GatewayMessage();
        toGateway.setToken(token);
        toGateway.setUserIds(new Long[]{userId});
        toGateway.setMessage(message);
        toGateway.setMessageId(message.getMessageId());
        GatewayRelation gatewayRelation = tokenGatewayMap.get(token);
        if (gatewayRelation != null) {
            gatewayRelation.gatewayChannelManager.sendMessage(toGateway);
            //关联userId
            userGatewayMap.put(userId, gatewayRelation);
        } else {
            logger.warn("token {} 不存在 GatewayRelation", token);
        }
    }

    /**
     * 用户主动离开该服务器后，调用该方法，断线不要调用
     * 与bytoken 调用一个就可以了
     * @param userId
     */
    public void sendBreakGatewayMessage2Gateway(Long userId) {
        GatewayRelation relation = userGatewayMap.get(userId);
        if (relation != null) {
            Server2GatewayMessage toGateway = new Server2GatewayMessage();
            toGateway.setUserIds(new Long[]{userId});
            toGateway.setMessage(new SCBreakUserGatewayMessage());
            toGateway.setMessageId(SCBreakUserGatewayMessage.MESSAGE_ID);
            relation.gatewayChannelManager.sendMessage(toGateway);
        }
    }

    /**
     * 用户主动离开该服务器后，调用该方法，断线不要调用
     * 与userid 调用一个就可以了
     * @param token
     */
    public void sendBreakGatewayMessageByToken2Gateway(Long token) {
        GatewayRelation relation = tokenGatewayMap.get(token);
        if (relation != null) {
            Server2GatewayMessage toGateway = new Server2GatewayMessage();
            toGateway.setToken(token);
            toGateway.setUserIds(new Long[0]);
            toGateway.setMessage(new SCBreakUserGatewayMessage());
            toGateway.setMessageId(SCBreakUserGatewayMessage.MESSAGE_ID);
            relation.gatewayChannelManager.sendMessage(toGateway);
        }
    }

    public void sendMessage2GatewayByToken(Long token, Message message) {
        Server2GatewayMessage toGateway = new Server2GatewayMessage();
        toGateway.setToken(token);
        toGateway.setUserIds(new Long[0]);
        toGateway.setMessage(message);
        toGateway.setMessageId(message.getMessageId());
        GatewayRelation gatewayRelation = tokenGatewayMap.get(token);
        if (gatewayRelation != null) {
            gatewayRelation.gatewayChannelManager.sendMessage(toGateway);
        } else {
            logger.warn("token {} 不存在 GatewayRelation", token);
        }
    }


    public void sendMessage2Gateway(Long userId, Message message) {
        Server2GatewayMessage toGateway = new Server2GatewayMessage();
        toGateway.setUserIds(new Long[]{userId});
        toGateway.setMessage(message);
        toGateway.setMessageId(message.getMessageId());
        GatewayRelation gatewayRelation = userGatewayMap.get(userId);
        if (gatewayRelation != null) {
            gatewayRelation.gatewayChannelManager.sendMessage(toGateway);
        } else {
            logger.warn("userId {} 不存在 GatewayRelation", userId);
        }

    }

    public void sendMessage2Gateway(List<Long> userIds, Message message) {
        Map<Integer, GatewayUsers> map = new HashMap<>();
        for (Long userId : userIds) {
            GatewayRelation gatewayRelation = userGatewayMap.get(userId);
            if (gatewayRelation != null) {
                Integer number = gatewayRelation.gatewayChannelManager.getGatewayChannelKey();
                GatewayUsers gatewayUsers = map.get(number);
                if (gatewayUsers == null) {
                    gatewayUsers = new GatewayUsers();
                    gatewayUsers.gatewayChannelManager = gatewayRelation.gatewayChannelManager;
                    map.put(number, gatewayUsers);
                }
                gatewayUsers.userIds.add(userId);
            } else {
                logger.warn("userIds -> userId {} 不存在 GatewayRelation", userId);
            }
        }
        map.values().forEach(gatewayUsers -> {
            Server2GatewayMessage toGateway = new Server2GatewayMessage();
            toGateway.setMessage(message);
            toGateway.setMessageId(message.getMessageId());
            Long[] users = new Long[gatewayUsers.userIds.size()];
            gatewayUsers.userIds.toArray(users);
            toGateway.setUserIds(users);
            gatewayUsers.gatewayChannelManager.sendMessage(toGateway);
        });
    }

    public void sendMessage2EveryOne(Message message) {
        Server2GatewayMessage toGateway = new Server2GatewayMessage();
        toGateway.setUserIds(new Long[]{0L});
        toGateway.setMessage(message);
        toGateway.setMessageId(message.getMessageId());
        dispatchMessage2Gateway(toGateway);
    }

    class GatewayUsers {
        List<Long> userIds = new ArrayList<>(16);
        GatewayChannelManager gatewayChannelManager;
    }

    class GatewayRelation {
        GatewayChannelManager gatewayChannelManager;
        Long relationToken;
    }
}
