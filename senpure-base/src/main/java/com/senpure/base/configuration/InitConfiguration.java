package com.senpure.base.configuration;

import com.senpure.base.util.Spring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * InitConfiguration
 *
 * @author senpure
 * @time 2018-10-25 10:14:10
 */
@Configuration
public class InitConfiguration {

    @Bean(value = "springIocInstance")
    public Spring spring() {
        return new Spring();
    }
}
