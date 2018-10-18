package com.senpure.io.handler;

import com.senpure.io.message.CSAskHandleMessage;
import io.netty.channel.Channel;

/**
 * 询问服务器是否可以处理该值得请求处理器
 *
 * @author senpure
 * @time 2018-10-17 14:59:15
 */
public class CSAskHandleMessageHandler extends AbstractRealityMessageHandler<CSAskHandleMessage> {

    @Override
    public void execute(Channel channel, long token, long userId, CSAskHandleMessage message) {
        //TODO 请在这里写下你的代码

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