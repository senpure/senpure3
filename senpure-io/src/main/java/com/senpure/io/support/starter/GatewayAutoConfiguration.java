package com.senpure.io.support.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * GatewayAutoConfiguration
 *
 * @author senpure
 * @time 2019-02-28 15:57:15
 */
public class GatewayAutoConfiguration {

    @Autowired
    private DiscoveryClient discoveryClient;
}
