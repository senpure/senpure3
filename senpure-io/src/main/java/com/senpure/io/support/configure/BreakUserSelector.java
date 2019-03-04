package com.senpure.io.support.configure;

import com.senpure.io.handler.CSBreakUserGatewayMessageHandler;
import com.senpure.io.support.annotation.EnableProducer;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * BreakUserSelector<br>
 * 有些服务器需要自己处理断开，如发送退出事件等
 *
 * @author senpure
 * @time 2019-03-04 11:50:05
 */
public class BreakUserSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annotationType = EnableProducer.class;
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(
                annotationType.getName(), false));
        if (attributes.getBoolean("breakUser")) {
            return new String[]{CSBreakUserGatewayMessageHandler.class.getName()};
        }
        return new String[0];

    }
}
