package com.senpure.base.annotation;

import com.senpure.base.configuration.SingleContextConfiguration;
import com.senpure.base.configuration.SpringContextCheckerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * EnableSingleContext
 * 只允许存在一个spring容器
 *
 * @author senpure
 * @time 2019-01-29 10:49:30
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({SingleContextConfiguration.class, SpringContextCheckerConfiguration.class})
public @interface EnableSingleContext {
}
