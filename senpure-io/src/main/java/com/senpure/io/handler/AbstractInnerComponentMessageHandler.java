package com.senpure.io.handler;


import com.senpure.io.message.Message;


public abstract class AbstractInnerComponentMessageHandler<T extends Message> extends AbstractComponentMessageHandler<T> {

    @Override
    public boolean regToGateway() {
        return false;
    }
}
