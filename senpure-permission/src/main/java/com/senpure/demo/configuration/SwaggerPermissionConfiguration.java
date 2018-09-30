package com.senpure.demo.configuration;

import com.senpure.base.annotation.ExtPermission;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
public class SwaggerPermissionConfiguration {
    @ExtPermission
    @Component
    class SwaggerPermission {
        @ExtPermission(value = "/v2/api-docs")
        public void apiDoc() {
        }
    }

}
