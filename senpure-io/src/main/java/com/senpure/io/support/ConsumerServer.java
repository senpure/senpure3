package com.senpure.io.support;

import com.senpure.io.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * ConsumerServer
 *
 * @author senpure
 * @time 2019-03-06 14:30:32
 */
public class ConsumerServer {

    private Logger logger = LoggerFactory.getLogger(ProducerServer.class);
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private ServerProperties serverProperties;
}
