package com.senpure.io.handler;

import com.senpure.io.message.CSBreakUserGatewayMessage;
import com.senpure.io.server.GatewayManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 断开用户与网关处理器
 *
 * @author senpure
 * @time 2018-10-19 16:14:32
 */
public class CSBreakUserGatewayMessageHandler extends AbstractRealityMessageHandler<CSBreakUserGatewayMessage> {
    @Autowired
    private GatewayManager gatewayManager;

    @Override
    public void execute(Channel channel, long token, long userId, CSBreakUserGatewayMessage message) {

        gatewayManager.breakToken(message.getToken(), message.getRelationToken());
        gatewayManager.breakUser(message.getUserId(), message.getRelationToken());

    }

    @Override
    public int handlerId() {
        return 1201;
    }

    @Override
    public CSBreakUserGatewayMessage getEmptyMessage() {
        return new CSBreakUserGatewayMessage();
    }
}