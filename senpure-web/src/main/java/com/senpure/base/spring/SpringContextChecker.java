package com.senpure.base.spring;

import com.senpure.base.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


public class SpringContextChecker implements
        ApplicationListener<ContextRefreshedEvent> {
    protected Logger logger;

    public static boolean ENABLE_MIX = false;
    private long lastRefreshTime = 0;

    private ApplicationContext lastApplicationContext;
    public SpringContextChecker() {
        this.logger = LoggerFactory.getLogger(getClass());
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.debug("{}: contextChecker :{}", event.getApplicationContext().getId());
        long now = System.currentTimeMillis();
        if (lastRefreshTime > 0) {
            if (now - lastRefreshTime < 5000 && !ENABLE_MIX) {
                StringBuilder sb = new StringBuilder();
                sb.append("存在多个spring context,如果确认需要使用多个 context 请使用注解 EnableMixContext");
                sb.append("\n");
                sb.append(lastApplicationContext);
                sb.append("\n");
                sb.append(event.getApplicationContext());
                Assert.error(sb.toString());
            }
        }
        lastRefreshTime = now;
        lastApplicationContext = event.getApplicationContext();
    }
}
