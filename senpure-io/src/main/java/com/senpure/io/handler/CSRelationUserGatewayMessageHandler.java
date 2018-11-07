package com.senpure.io.handler;

import com.senpure.io.ChannelAttributeUtil;
import com.senpure.io.message.CSRelationUserGatewayMessage;
import com.senpure.io.message.SCRelationUserGatewayMessage;
import com.senpure.io.server.GatewayManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 关联用户与网关处理器
 *
 * @author senpure
 * @time 2018-10-17 14:59:15
 */
public class CSRelationUserGatewayMessageHandler extends AbstractInnerMessageHandler<CSRelationUserGatewayMessage> {

    @Autowired
    private GatewayManager gatewayManager;

    @Override
    public void execute(Channel channel, long token, long userId, CSRelationUserGatewayMessage message) {
        String gatewayKey = ChannelAttributeUtil.getIpAndPort(channel);
        logger.debug("关联网关 与用户 gatewayKey :{}  token :{}  userId :{}", gatewayKey, message.getToken(), message.getUserId());
        if (message.getUserId() > 0) {
            gatewayManager.relationUser(gatewayKey, message.getUserId(), message.getRelationToken());
        }
        if (message.getToken() != 0) {
            gatewayManager.relationToken(gatewayKey, message.getToken(), message.getRelationToken());
        }
        SCRelationUserGatewayMessage scMessage = new SCRelationUserGatewayMessage();
        scMessage.setRelationToken(message.getRelationToken());
        scMessage.setToken(message.getToken());
        scMessage.setUserId(message.getUserId());
        if (message.getUserId() > 0) {
            gatewayManager.sendMessage2Gateway(message.getUserId(), scMessage);
        } else {
            gatewayManager.sendMessage2GatewayByToken(message.getToken(), scMessage);
        }

    }

    @Override
    public int handlerId() {
        return 1101;
    }

    @Override
    public CSRelationUserGatewayMessage getEmptyMessage() {
        return new CSRelationUserGatewayMessage();
    }
}