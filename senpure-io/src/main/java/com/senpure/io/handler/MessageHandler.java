package com.senpure.io.handler;

import com.senpure.io.message.Message;
import io.netty.channel.Channel;



public interface MessageHandler<T extends Message> {


    T getEmptyMessage();
    void execute(Channel channel, T message);
    int handlerId();


}
