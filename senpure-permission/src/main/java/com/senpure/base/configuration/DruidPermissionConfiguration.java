package com.senpure.base.configuration;

import com.senpure.base.annotation.ExtPermission;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;


@Configuration
public class DruidPermissionConfiguration {

    @Component
    @ExtPermission
    class ActuatorPermission {

        @ExtPermission(value = {"/druid","/druid/**.*","/druid/**/*.*"}, name = "/druid_read")
        public void readDruid() {
        }
        @ExtPermission(value = {"/druid/**.*"}, name = "/druid_read",method = RequestMethod.POST)
        public void readDruidJson() {
        }


    }
}
