package com.senpure.io.support.annotation;

import com.senpure.io.ServerProperties;
import com.senpure.io.support.configure.GatewayAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * EnableGateway
 *
 * @author senpure
 * @time 2019-03-01 11:49:08
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@EnableConfigurationProperties({ServerProperties.class})
@Import({GatewayAutoConfiguration.class})
public @interface EnableGateway {
}
