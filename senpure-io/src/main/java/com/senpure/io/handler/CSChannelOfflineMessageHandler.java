package com.senpure.io.handler;

import com.senpure.io.message.CSChannelOfflineMessage;
import io.netty.channel.Channel;

/**
 * 客户端掉线处理器
 *
 * @author senpure
 * @time 2018-10-17 14:59:15
 */
public class CSChannelOfflineMessageHandler extends AbstractRealityMessageHandler<CSChannelOfflineMessage> {

    @Override
    public void execute(Channel channel, long token, long userId, CSChannelOfflineMessage message) {
        //TODO 请在这里写下你的代码

    }

    @Override
    public int handlerId() {
        return 1103;
    }

    @Override
    public CSChannelOfflineMessage getEmptyMessage() {
        return new CSChannelOfflineMessage();
    }
}