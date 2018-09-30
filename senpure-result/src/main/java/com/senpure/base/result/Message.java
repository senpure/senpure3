package com.senpure.base.result;

import java.lang.annotation.*;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Message {
    String message() default "" ;

    String describe() default "";
}
