package com.senpure.base.result;

import java.lang.annotation.*;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Message {
    /**
     * 错误消息
     *
     * @return
     */
    String value() default "";

    /**
     * 资源文件的key
     *
     * @return
     */
    String key() default "";

}
