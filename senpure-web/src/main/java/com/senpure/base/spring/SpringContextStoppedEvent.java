package com.senpure.base.spring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;


public class SpringContextStoppedEvent implements
        ApplicationListener<ContextStoppedEvent> {
    protected Logger logger;

    public SpringContextStoppedEvent() {
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void onApplicationEvent(ContextStoppedEvent event) {
        if (SpringContextUtil.isTopContext(event.getApplicationContext())) {
            logger.debug("{}: stoppedEvent :{}", event.getApplicationContext().getId(), event.toString());
        }
    }

}
