package com.senpure.base.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;


@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExtPermission {
    /**
     * uri
     * @return
     */
    String[] value() default {};
    /**
     * 唯一标识
     *
     * @return
     */
    String name() default "";
    String readableName() default "";
    RequestMethod[] method() default {RequestMethod.GET};

}
