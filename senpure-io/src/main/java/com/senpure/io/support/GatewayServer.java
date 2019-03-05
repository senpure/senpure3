package com.senpure.io.support;

import com.senpure.base.util.NameThreadFactory;
import com.senpure.io.ServerProperties;
import com.senpure.io.gateway.GatewayAndClientServer;
import com.senpure.io.gateway.GatewayAndServerServer;
import com.senpure.io.gateway.GatewayMessageExecuter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * GatewayServer
 *
 * @author senpure
 * @time 2019-03-01 15:17:33
 */
public class GatewayServer implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ServerProperties properties;
    private GatewayMessageExecuter messageExecuter;
    private GatewayAndClientServer gatewayAndClientServer;
    private GatewayAndServerServer gatewayAndServerServer;


    public void start() {
        check();
        messageExecuter();
        servers();
    }

    private void check() {
        if (StringUtils.isEmpty(properties.getName())) {
            properties.setName("gateway");
        }

        ServerProperties.Gateway gateway = properties.getGateway();
        if (!gateway.isSetReadableName()) {
            gateway.setReadableName(properties.getName());
        }
        //io *2 logic *1 综合1.5
        double size = Runtime.getRuntime().availableProcessors() * 1.5;
        int ioSize = (int) (size * 0.6);
        ioSize = ioSize < 1 ? 1 : ioSize;
        int logicSize = (int) (size * 0.4);
        logicSize = logicSize < 1 ? 1 : logicSize;
        if (gateway.getExecuterThreadPoolSize() < 1) {
            gateway.setExecuterThreadPoolSize(logicSize);
        }
        if (gateway.getIoCsWorkThreadPoolSize() < 1) {
            int workSize = ioSize << 1;
            workSize = workSize < 1 ? 1 : workSize;
            gateway.setIoCsWorkThreadPoolSize(workSize);
        }
        if (gateway.getIoScWorkThreadPoolSize() < 1) {
            int workSize = ioSize << 1;
            workSize = workSize < 1 ? 1 : workSize;
            gateway.setIoScWorkThreadPoolSize(workSize);
        }
        logger.info(gateway.toString());
    }

    private void messageExecuter() {
        ServerProperties.Gateway gateway = properties.getGateway();
        ScheduledExecutorService service = Executors.newScheduledThreadPool(gateway.getExecuterThreadPoolSize(),
                new NameThreadFactory(properties.getName() + "-executor"));
        messageExecuter = new GatewayMessageExecuter(service);
        messageExecuter.setCsLoginMessageId(gateway.getCsLoginMessageId());
        messageExecuter.setScLoginMessageId(gateway.getScLoginMessageId());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


    }

    private void servers() {
        GatewayAndClientServer gatewayAndClientServer = new GatewayAndClientServer();
        gatewayAndClientServer.setMessageExecuter(messageExecuter);
        gatewayAndClientServer.setProperties(properties.getGateway());
        if (gatewayAndClientServer.start()) {
            this.gatewayAndClientServer = gatewayAndClientServer;
        }
        GatewayAndServerServer gatewayAndServerServer = new GatewayAndServerServer();
        gatewayAndServerServer.setMessageExecuter(messageExecuter);
        gatewayAndServerServer.setProperties(properties.getGateway());
        if (gatewayAndServerServer.start()) {
            this.gatewayAndServerServer = gatewayAndServerServer;
        }
    }

    @PreDestroy
    public void destroy() {
        if (gatewayAndClientServer != null) {
            gatewayAndClientServer.destroy();
        }
        if (gatewayAndServerServer != null) {
            gatewayAndServerServer.destroy();
        }
        if (messageExecuter != null) {
            messageExecuter.shutdownService();
        }
    }

    public void setProperties(ServerProperties properties) {
        this.properties = properties;
    }

    public static void main(String[] args) {

    }


}
