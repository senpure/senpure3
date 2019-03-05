package com.senpure.io.support.configure;

import com.senpure.io.ServerProperties;
import com.senpure.io.support.GatewayServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * GatewayAutoConfiguration
 *
 * @author senpure
 * @time 2019-02-28 16:52:21
 */
public class GatewayAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ServerProperties properties;


    @Bean
    public GatewayServer gatewayServer() {
        GatewayServer gatewayServer = new GatewayServer();
        gatewayServer.setProperties(properties);
        gatewayServer.start();
        return gatewayServer;
    }

}
