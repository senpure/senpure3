package com.senpure.base.annotation;


public @interface Cache {
    boolean value() default  true;
    boolean remote() default  true;
    boolean local() default  false;
    boolean map() default  false;

}
