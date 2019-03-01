package com.senpure.io.support.annotation;

import com.senpure.io.support.configure.ProducerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * EnableProducer
 *
 * @author senpure
 * @time 2019-03-01 11:50:10
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({ProducerAutoConfiguration.class})
public @interface EnableProducer {
}
