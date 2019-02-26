package com.senpure.io.handler;

import com.senpure.io.message.SCInnerErrorMessage;
import io.netty.channel.Channel;

/**
 * SCInnerErrorMessageHandler
 *
 * @author senpure
 * @time 2019-02-20 16:29:14
 */
public class SCInnerErrorMessageHandler extends AbstractMessageHandler<SCInnerErrorMessage> {
    @Override
    public void execute(Channel channel, SCInnerErrorMessage message) throws Exception {
        logger.debug(message.toString());
    }

    @Override
    public int handlerId() {
        return SCInnerErrorMessage.MESSAGE_ID;
    }

    @Override
    public SCInnerErrorMessage getEmptyMessage() {
        return new SCInnerErrorMessage();
    }
}
