package com.senpure.base.configuration;

import com.senpure.base.spring.SpringContextClosedEvent;
import com.senpure.base.spring.SpringContextRefreshEvent;
import com.senpure.base.spring.SpringContextStartedEvent;
import com.senpure.base.spring.SpringContextStoppedEvent;
import org.springframework.context.annotation.Bean;

/**
 * SpringEventRecordConfiguration
 *
 * @author senpure
 * @time 2019-01-29 13:50:37
 */
public class SpringEventRecordConfiguration {


    @Bean
    public SpringContextRefreshEvent springContextRefreshEvent() {
        return new SpringContextRefreshEvent();
    }

    @Bean
    public SpringContextStoppedEvent springContextStoppedEvent() {
        return new SpringContextStoppedEvent();
    }

    @Bean
    public SpringContextStartedEvent springContextStartedEvent() {
        return new SpringContextStartedEvent();
    }

    @Bean
    public SpringContextClosedEvent springContextClosedEvent() {
        return new SpringContextClosedEvent();
    }
}
