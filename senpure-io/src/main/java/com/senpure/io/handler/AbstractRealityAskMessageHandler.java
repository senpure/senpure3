package com.senpure.io.handler;


import com.senpure.io.protocol.Message;


public abstract class AbstractRealityAskMessageHandler<T extends Message> extends AbstractRealityMessageHandler<T> implements RealityAskMessageHandler<T> {


    @Override
    public boolean direct() {
        return false;
    }

    @Override
    public boolean serverShare() {
        return true;
    }

    @Override
    public boolean regToGateway() {
        return true;
    }
}
