package com.senpure.io.server;

import com.senpure.io.ChannelAttributeUtil;
import com.senpure.io.ComponentMessageHandlerUtil;
import com.senpure.io.handler.ComponentMessageHandler;
import com.senpure.io.message.CSRelationPlayerGatewayMessage;
import com.senpure.io.message.Gateway2ServerMessage;
import com.senpure.io.message.Message;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RealityMessageExecuter {

    private ExecutorService service;
    private GatewayManager gatewayServer;
    private CSRelationPlayerGatewayMessage message = new CSRelationPlayerGatewayMessage();

    private int relationMessageId = message.getMessageId();


    public RealityMessageExecuter(GatewayManager gatewayServer) {
        this.gatewayServer = gatewayServer;
        service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }

    public RealityMessageExecuter(ExecutorService service, GatewayManager gatewayServer) {
        this.service = service;
        this.gatewayServer = gatewayServer;
    }

    private Logger logger = LoggerFactory.getLogger(RealityMessageExecuter.class);

    public void execute(Runnable runnable) {
        service.execute(runnable);
    }

    public void execute(Channel channel, Gateway2ServerMessage gsMessage) {
        service.execute(() -> {
            if (gsMessage.getMessageId() == relationMessageId) {
                CSRelationPlayerGatewayMessage relationMessage = new CSRelationPlayerGatewayMessage();
                relationMessage.read(gsMessage.getBuf());

                logger.debug(relationMessage.toString());
                String gatewayKey = ChannelAttributeUtil.getIpAndPort(channel);
                logger.debug("gatewayKey :{}", gatewayKey);
                if (relationMessage.getPlayerId() > 0) {
                    gatewayServer.relationPlayer(gatewayKey, relationMessage.getPlayerId());
                }
                if (relationMessage.getToken() != 0) {
                    gatewayServer.relationToken(gatewayKey, relationMessage.getToken());
                }
                return;
            }
            long playerId = gsMessage.getPlayerId();
            ComponentMessageHandler handler = ComponentMessageHandlerUtil.getHandler(gsMessage.getMessageId());
            if (handler == null) {
                logger.warn("没有找到消息出来程序{} playerId:{}", gsMessage.getMessageId(), playerId);
                return;
            }
            Message message = handler.getEmptyMessage();
            message.read(gsMessage.getBuf());
            handler.execute(channel, gsMessage.getToken(), playerId, message);
            //  ComponentMessageHandlerUtil.execute(gsMessage.getToken(),playerId,message);

        });
    }


}
