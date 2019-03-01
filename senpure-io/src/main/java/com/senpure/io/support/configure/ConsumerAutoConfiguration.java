package com.senpure.io.support.configure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * ConsumerAutoConfiguration
 *
 * @author senpure
 * @time 2019-03-01 11:46:50
 */

public class ConsumerAutoConfiguration {
    private Logger logger = LoggerFactory.getLogger(getClass());
    public ConsumerAutoConfiguration() {
        ModuleStatistics.enableProducer();

    }
    @Bean("b")
    public String a() {
        logger.info("模块启动数量{}  {} {} {} ", ModuleStatistics.getModuleAmount(),ModuleStatistics.isEnableGateway(),ModuleStatistics.isEnableConsumer(),ModuleStatistics.isEnableProducer());
        return "bean-b";
    }
}
