package com.senpure.base.annotation;

import com.senpure.base.configuration.MixContextConfiguration;
import com.senpure.base.configuration.SpringContextCheckerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * EnableSingleContext
 * 允许存在多个spring容器
 *
 * @author senpure
 * @time 2019-01-29 10:49:30
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({MixContextConfiguration.class, SpringContextCheckerConfiguration.class})
public @interface EnableMixContext {
}
