package com.senpure.io.support;

import com.senpure.io.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ProducerChecker
 *
 * @author senpure
 * @time 2019-03-04 17:51:39
 */
public class ProducerServer implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(ProducerServer.class);
    private DiscoveryClient discoveryClient;

    private ServerProperties.Producer producer;
    private ServerProperties.Gateway gateway = new ServerProperties.Gateway();
    private ScheduledExecutorService service;

    private Map<String, Boolean> gatewayMap = new HashMap<>();

    public ProducerServer(DiscoveryClient discoveryClient, ServerProperties.Producer producer, ScheduledExecutorService service) {
        this.discoveryClient = discoveryClient;
        this.producer = producer;
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                List<ServiceInstance> serviceInstances = discoveryClient.getInstances(producer.getGatewayName());
                for (ServiceInstance instance : serviceInstances) {
                    String port = instance.getMetadata().get("port");
                    if (port == null) {
                        logger.warn("网关 {} 没有 没有配置socket端口", producer.getGatewayName());
                        logger.warn("网关 {} 没有 没有配置socket端口", producer.getGatewayName());
                        logger.warn("网关 {} 没有 没有配置socket端口", producer.getGatewayName());

                    } else {
                        String serverKey = instance.getHost() + ":" + port;
                    }

                }
            }
        }, 2000, 50, TimeUnit.MILLISECONDS);
    }
}
