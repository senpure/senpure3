package com.senpure.io.event.handler;

import com.senpure.io.event.EventHelper;
import com.senpure.io.protocol.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * AbstractEventHandler
 *
 * @author senpure
 * @time 2018-11-16 11:31:29
 */
public abstract class AbstractEventHandler<T extends Event> implements EventHandler<T>, InitializingBean {

    protected Logger logger;
    public AbstractEventHandler() {
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EventHelper.regEventHandler(this);
    }
}
