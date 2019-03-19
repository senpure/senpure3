package com.senpure.io.support.annotation;

import com.senpure.io.support.configure.ConsumerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * EnableConsumer
 *
 * @author senpure
 * @time 2019-03-01 11:51:26
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({ConsumerAutoConfiguration.class})
@Deprecated
public @interface EnableConsumer {
}
