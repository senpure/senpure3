package com.senpure.base.configuration;

import com.senpure.base.spring.SpringContextChecker;
import org.springframework.context.annotation.Bean;

/**
 * SingleContextConfiguration
 *
 * @author senpure
 * @time 2019-01-29 10:50:22
 */
public class SpringContextCheckerConfiguration {

    @Bean
    public SpringContextChecker springContextChecker() {
        SpringContextChecker springContextChecker = new SpringContextChecker();
        return springContextChecker;
    }
}
