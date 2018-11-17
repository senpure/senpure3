package com.senpure.io.handler;

import com.senpure.base.util.Assert;
import com.senpure.io.event.EventHelper;
import com.senpure.io.protocol.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ResolvableType;

/**
 * AbstractEventHandler
 *
 * @author senpure
 * @time 2018-11-16 11:31:29
 */
public abstract class AbstractEventHandler<T extends Event> implements EventHandler<T>, InitializingBean {

    protected Logger logger;
    protected Class<T> eventClass;

    public AbstractEventHandler() {
        logger = LoggerFactory.getLogger(getClass());
        ResolvableType resolvableType = ResolvableType.forClass(getClass());
        eventClass = (Class<T>) resolvableType.getSuperType().getGeneric(0).resolve();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EventHelper.regEventHandler(this);
    }

    @Override
    public T getEmptyEvent() {
        try {
            return eventClass.newInstance();
        } catch (Exception e) {

            Assert.error(e.toString());
        }
        return null;
    }
}
