package com.senpure.io.handler;

import com.senpure.io.RealityMessageHandlerUtil;
import com.senpure.io.message.CSAskHandleMessage;
import com.senpure.io.message.SCAskHandleMessage;
import com.senpure.io.message.Server2GatewayMessage;
import com.senpure.io.server.GatewayManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 询问服务器是否可以处理该值得请求处理器
 *
 * @author senpure
 * @time 2018-10-17 14:59:15
 */
public class CSAskHandleMessageHandler extends AbstractInnerMessageHandler<CSAskHandleMessage> {

    @Autowired
    private GatewayManager gatewayManager;
    @Override
    public void execute(Channel channel, long token, long userId, CSAskHandleMessage message) {
        RealityMessageHandler realityMessageHandler = RealityMessageHandlerUtil.getHandler(message.getFromMessageId());
        RealityAskMessageHandler askMessageHandler = (RealityAskMessageHandler) realityMessageHandler;

        SCAskHandleMessage scAskHandleMessage = askMessageHandler.ask(message);
        if (scAskHandleMessage != null) {
            Server2GatewayMessage toGateway = new Server2GatewayMessage();
            toGateway.setToken(token);
            toGateway.setUserIds(new Long[0]);
            toGateway.setMessage(message);
            toGateway.setMessageId(message.getMessageId());
            channel.writeAndFlush(toGateway);
        }
    }

    @Override
    public int handlerId() {
        return 1105;
    }

    @Override
    public CSAskHandleMessage getEmptyMessage() {
        return new CSAskHandleMessage();
    }
}