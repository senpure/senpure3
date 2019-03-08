package com.senpure.io.handler;

import com.senpure.io.message.CSHeartMessage;
import io.netty.channel.Channel;

/**
 * 询问服务器是否可以处理该值得请求处理器
 *
 * @author senpure
 * @time 2018-10-17 14:59:15
 */
public class CSHeartMessageHandler extends AbstractInnerMessageHandler<CSHeartMessage> {

    @Override
    public void execute(Channel channel, long token, long userId, CSHeartMessage message) {

    }

    @Override
    public int handlerId() {
        return CSHeartMessage.MESSAGE_ID;
    }

    @Override
    public CSHeartMessage getEmptyMessage() {
        return new CSHeartMessage();
    }
}