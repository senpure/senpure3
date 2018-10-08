package com.senpure.io.server;

import com.senpure.io.message.Message;
import com.senpure.io.message.Server2GatewayMessage;
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

    private ConcurrentMap<Integer, GatewayChannelManager> playerGatewayMap = new ConcurrentHashMap<>();

    private ConcurrentMap<Integer, GatewayChannelManager> tokenGatewayMap = new ConcurrentHashMap<>();

    public synchronized GatewayChannelManager getComponentGatewayChannelServer(String serverKey) {

        GatewayChannelManager server = gatewayChannelMap.get(serverKey);
        if (server == null) {
            server = new GatewayChannelManager();
            gatewayChannelMap.put(serverKey, server);
            return gatewayChannelMap.get(serverKey);
        }
        return server;
    }

    public void relationPlayer(String serverKey, Integer playerId) {
        playerGatewayMap.putIfAbsent(playerId, gatewayChannelMap.get(serverKey));
    }

    public void relationToken(String serverKey, Integer token) {
        tokenGatewayMap.putIfAbsent(token, gatewayChannelMap.get(serverKey));
    }

    public void breakPlayer(Integer playerId) {
        playerGatewayMap.remove(playerId);
    }

    public void breakToken(Integer token) {
        tokenGatewayMap.remove(token);
    }

    public void offline(int token, int playerId) {
        logger.debug("token {} id {}离线", token, playerId);
        if (token != 0) {
            breakToken(token);
        }
        if (playerId > 0) {
            breakPlayer(playerId);
        }
    }

    public boolean canHandleMessageValue(int messageId,String value)
    {
        return  false;
    }

    public void sendMessage2GatewayByToken(Long token, Message message) {
        Server2GatewayMessage toGateway = new Server2GatewayMessage();
        toGateway.setToken(token);
        toGateway.setPlayerIds(new Long[0]);
        toGateway.setMessage(message);
        toGateway.setMessageId(message.getMessageId());
        GatewayChannelManager channelServer = tokenGatewayMap.get(token);
        Channel channel = channelServer.nextChannel();
        channel.writeAndFlush(toGateway);

    }

    public void sendMessage2Gateway(Long playerId, Message message) {
        Server2GatewayMessage toGateway = new Server2GatewayMessage();
        toGateway.setPlayerIds(new Long[]{playerId});

        toGateway.setMessage(message);
        toGateway.setMessageId(message.getMessageId());
        playerGatewayMap.get(playerId).nextChannel().writeAndFlush(toGateway);

    }

    public void sendMessage2Gateway(List<Integer> playerIds, Message message) {
        Map<Integer, GatewayPlayers> map = new HashMap<>();
        for (Integer playerId : playerIds) {
            GatewayChannelManager gatewayChannelServer = playerGatewayMap.get(playerId);
            Integer number = gatewayChannelServer.getNumber();
            GatewayPlayers gatewayPlayers = map.get(number);
            if (gatewayPlayers == null) {
                gatewayPlayers = new GatewayPlayers();
                gatewayPlayers.gatewayChannelServer = gatewayChannelServer;
                map.put(number, gatewayPlayers);
            }
            gatewayPlayers.playerIds.add(playerId);
        }
        map.values().forEach(gatewayPlayers -> {
            Server2GatewayMessage toGateway = new Server2GatewayMessage();
            toGateway.setMessage(message);
            toGateway.setMessageId(message.getMessageId());
            Integer[] players = new Integer[gatewayPlayers.playerIds.size()];

            gatewayPlayers.playerIds.toArray(players);

            gatewayPlayers.gatewayChannelServer.nextChannel().writeAndFlush(toGateway);

        });

    }

    class GatewayPlayers {
        List<Integer> playerIds = new ArrayList<>(16);
        GatewayChannelManager gatewayChannelServer;
    }
}
