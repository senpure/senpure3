package com.senpure.base.annotation;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Retry {
    int retryTimes() default  3;
    int interval() default 100;
}
