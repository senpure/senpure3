package com.senpure.base.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * SpringContextUtil
 *
 * @author senpure
 * @time 2019-03-07 15:04:07
 */
public class SpringContextUtil {

    public static boolean isTopContext(ApplicationContext context) {
        return context.getParent() == null || context.getParent() instanceof AnnotationConfigApplicationContext;
    }
}
