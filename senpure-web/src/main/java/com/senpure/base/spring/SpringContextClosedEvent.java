package com.senpure.base.spring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;


public class SpringContextClosedEvent implements ApplicationListener<ContextClosedEvent> {
	protected   Logger logger;

	public SpringContextClosedEvent() {
		logger= LoggerFactory.getLogger(getClass());
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		if (SpringContextUtil.isTopContext(event.getApplicationContext())) {
			logger.debug("{}: closedEvent :{}",   event.getApplicationContext().getId(),event.toString());
		}
	}
}
