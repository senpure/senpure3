package com.senpure.io.handler;


import com.senpure.base.util.Assert;
import com.senpure.io.RealityMessageHandlerUtil;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.GatewayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;


public abstract class AbstractRealityMessageHandler<T extends Message> implements RealityMessageHandler<T>, InitializingBean {
    protected Logger logger;
    protected Class<T> messageClass;
    @Autowired
    protected GatewayManager gatewayManager;

    public AbstractRealityMessageHandler() {
        this.logger = LoggerFactory.getLogger(getClass());
        ResolvableType resolvableType = ResolvableType.forClass(getClass());
        messageClass = (Class<T>) resolvableType.getSuperType().getGeneric(0).resolve();
    }

    @Override
    public T getEmptyMessage() {

        try {
            return messageClass.newInstance();
        } catch (Exception e) {

            Assert.error(e.toString());
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RealityMessageHandlerUtil.regMessageHandler(this);

    }


    @Override
    public boolean direct() {
        return true;
    }

    @Override
    public boolean serverShare() {
        return false;
    }

    @Override
    public boolean regToGateway() {
        return true;
    }
}
