package com.senpure.io.handler;

import com.senpure.io.RealityMessageHandlerUtil;
import com.senpure.io.message.CSAskHandleMessage;
import com.senpure.io.message.SCAskHandleMessage;
import com.senpure.io.message.Server2GatewayMessage;
import io.netty.channel.Channel;

/**
 * 询问服务器是否可以处理该值得请求处理器
 *
 * @author senpure
 * @time 2018-10-17 14:59:15
 */
public class CSAskHandleMessageHandler extends AbstractInnerMessageHandler<CSAskHandleMessage> {

    @Override
    public void execute(Channel channel, long token, long userId, CSAskHandleMessage message) {
        RealityMessageHandler realityMessageHandler = RealityMessageHandlerUtil.getHandler(message.getFromMessageId());
        RealityAskMessageHandler askMessageHandler = (RealityAskMessageHandler) realityMessageHandler;

        SCAskHandleMessage scAskHandleMessage = askMessageHandler.ask(message);
        if (scAskHandleMessage == null) {
            scAskHandleMessage = new SCAskHandleMessage();
            scAskHandleMessage.setFromMessageId(message.getFromMessageId());
            scAskHandleMessage.setHandle(false);
            scAskHandleMessage.setToken(message.getToken());
            scAskHandleMessage.setValue(message.getValue());
        }
        Server2GatewayMessage toGateway = new Server2GatewayMessage();
        toGateway.setToken(token);
        toGateway.setUserIds(new Long[0]);
        toGateway.setMessage(scAskHandleMessage);
        toGateway.setMessageId(scAskHandleMessage.getMessageId());
        channel.writeAndFlush(toGateway);

    }

    @Override
    public int handlerId() {
        return CSAskHandleMessage.MESSAGE_ID;
    }

    @Override
    public CSAskHandleMessage getEmptyMessage() {
        return new CSAskHandleMessage();
    }
}