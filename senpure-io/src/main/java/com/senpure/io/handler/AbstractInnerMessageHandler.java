package com.senpure.io.handler;


import com.senpure.io.protocol.Message;


public abstract class AbstractInnerMessageHandler<T extends Message> extends AbstractRealityMessageHandler<T> {

    @Override
    public boolean regToGateway() {
        return false;
    }
}
