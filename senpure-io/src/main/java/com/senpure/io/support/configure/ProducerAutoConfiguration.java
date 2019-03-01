package com.senpure.io.support.configure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * ProducerAutoConfiguration
 *
 * @author senpure
 * @time 2019-03-01 11:46:50
 */
public class ProducerAutoConfiguration {
    private Logger logger = LoggerFactory.getLogger(getClass());
    public ProducerAutoConfiguration() {
        ModuleStatistics.enableProducer();

    }

    @Bean("c")
    public String a() {
        logger.info("模块启动数量{}  {} {} {}", ModuleStatistics.getModuleAmount(),ModuleStatistics.isEnableGateway(),ModuleStatistics.isEnableConsumer(),ModuleStatistics.isEnableProducer());
        return "bean-c";
    }
}
