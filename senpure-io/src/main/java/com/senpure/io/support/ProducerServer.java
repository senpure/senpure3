package com.senpure.io.support;

import com.senpure.base.util.NameThreadFactory;
import com.senpure.io.ChannelAttributeUtil;
import com.senpure.io.RealityMessageHandlerUtil;
import com.senpure.io.ServerProperties;
import com.senpure.io.bean.HandleMessage;
import com.senpure.io.bean.IdName;
import com.senpure.io.event.EventHelper;
import com.senpure.io.handler.RealityMessageHandler;
import com.senpure.io.message.SCIdNameMessage;
import com.senpure.io.message.SCRegServerHandleMessageMessage;
import com.senpure.io.message.Server2GatewayMessage;
import com.senpure.io.server.GatewayChannelManager;
import com.senpure.io.server.GatewayManager;
import com.senpure.io.server.RealityMessageExecuter;
import com.senpure.io.server.RealityServer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * ProducerChecker
 *
 * @author senpure
 * @time 2019-03-04 17:51:39
 */
public class ProducerServer implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(ProducerServer.class);
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private GatewayManager gatewayManager;

    private ServerProperties.Producer producer;
    private ServerProperties.Gateway gateway = new ServerProperties.Gateway();
    private ScheduledExecutorService service;

    private List<RealityServer> servers = new ArrayList<>();
    private RealityMessageExecuter messageExecuter;

    private Map<String, Long> failGatewayMap = new HashMap<>();

    private long lastLogTime = 0;

    private void check() {


        if (StringUtils.isEmpty(serverProperties.getName())) {
            serverProperties.setName("realityServer");
        }
        producer = serverProperties.getProducer();
        if (!producer.isSetReadableName()) {
            producer.setReadableName(serverProperties.getName());
        }
        //io *2 logic *1 综合1.5
        double size = Runtime.getRuntime().availableProcessors() * 1.5;
        int ioSize = (int) (size * 0.6);
        ioSize = ioSize < 1 ? 1 : ioSize;
        int logicSize = (int) (size * 0.4);
        logicSize = logicSize < 1 ? 1 : logicSize;
        if (producer.getIoWorkThreadPoolSize() < 1) {
            producer.setIoWorkThreadPoolSize(ioSize);
        }
        if (producer.getExecuterThreadPoolSize() < 1) {
            producer.setEventThreadPoolSize(logicSize);
        }
    }

    private void messageExecuter() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(gateway.getExecuterThreadPoolSize(),
                new NameThreadFactory(serverProperties.getName() + "-executor"));
        RealityMessageExecuter messageExecuter = new RealityMessageExecuter(service);
        this.messageExecuter = messageExecuter;
        this.service = service;
        EventHelper.setService(service);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        check();
        messageExecuter();
        List<Integer> ids = RealityMessageHandlerUtil.getHandlerMessageIds();

        List<HandleMessage> handleMessages = new ArrayList<>();
        for (Integer id : ids) {
            HandleMessage handleMessage = new HandleMessage();
            handleMessage.setHandleMessageId(id);
            RealityMessageHandler handler = RealityMessageHandlerUtil.getHandler(id);
            handleMessage.setDirect(handler.direct());
            handleMessage.setServerShare(handler.serverShare());
            handleMessage.setMessageClasses(RealityMessageHandlerUtil.getEmptyMessage(id).getClass().getName());
            handleMessages.add(handleMessage);
        }
        List<IdName> idNames = null;
        if (StringUtils.isNotEmpty(producer.getIdNamesPackage())) {
            idNames = MessageScanner.scan(producer.getIdNamesPackage());
        }
        List<IdName> finalIdNames = idNames;
        service.scheduleWithFixedDelay(() -> {
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(producer.getGatewayName());
            for (ServiceInstance instance : serviceInstances) {
                String portStr = instance.getMetadata().get("scPort");
                int port = 0;
                if (portStr == null) {
                    // logger.warn("网关 [{}] {} {} 没有 没有配置sc socket端口,使用默认端口 {}", producer.getGatewayName(), instance.getHost(), instance.getUri(), gateway.getScPort());
                    port = gateway.getScPort();
                } else {
                    port = Integer.parseInt(portStr);
                }
                String gatewayKey = gatewayManager.getGatewayKey(instance.getHost(), port);
                GatewayChannelManager gatewayChannelManager = gatewayManager.getGatewayChannelServer(gatewayKey);
                if (gatewayChannelManager.isConnecting()) {
                    continue;
                }
                long now = System.currentTimeMillis();
                if (gatewayChannelManager.getChannelSize() < producer.getGatewayChannel()) {
                    Long lastFailTime = failGatewayMap.get(gatewayKey);
                    boolean start = false;
                    if (lastFailTime == null) {
                        start = true;
                    } else {
                        if (now - lastFailTime >= producer.getConnectFailInterval()) {
                            start = true;
                        }
                    }
                    if (start) {
                        gatewayChannelManager.setConnecting(true);
                        RealityServer realityServer = new RealityServer();
                        realityServer.setGatewayManager(gatewayManager);
                        realityServer.setProperties(producer);
                        realityServer.setMessageExecuter(messageExecuter);
                        realityServer.setServerName(serverProperties.getName());
                        realityServer.setReadableServerName(producer.getReadableName());
                        if (realityServer.start(instance.getHost(), port)) {
                            servers.add(realityServer);
                            regServer(realityServer, handleMessages);
                            if (gatewayChannelManager.getChannelSize() == 1 && finalIdNames != null && finalIdNames.size() > 0) {
                                regIdNames(realityServer, finalIdNames);
                            }
                        } else {
                            failGatewayMap.put(gatewayKey, now);
                        }
                        gatewayChannelManager.setConnecting(false);
                    }
                }
            }
            long now = System.currentTimeMillis();
            if (now - lastLogTime >= 60000) {
                lastLogTime = now;
                gatewayManager.report();
            }
        }, 2000, 50, TimeUnit.MILLISECONDS);
    }

    public void regServer(RealityServer server, List<HandleMessage> handleMessages) {
        SCRegServerHandleMessageMessage message = new SCRegServerHandleMessageMessage();
        message.setServerName(serverProperties.getName());
        message.setReadableServerName(server.getReadableServerName());
        message.setServerKey(ChannelAttributeUtil.getLocalServerKey(server.getChannel()));
        message.setMessages(handleMessages);
        Server2GatewayMessage gatewayMessage = new Server2GatewayMessage();
        gatewayMessage.setMessageId(message.getMessageId());
        gatewayMessage.setMessage(message);
        gatewayMessage.setUserIds(new Long[]{0L});
        logger.debug("向{}注册服务{}", ChannelAttributeUtil.getRemoteServerKey(server.getChannel()), message);
        server.getChannel().writeAndFlush(gatewayMessage);
    }

    public void regIdNames(RealityServer server, List<IdName> idNames) {
        SCIdNameMessage message = new SCIdNameMessage();
        for (int i = 0; i < idNames.size(); i++) {
            if (i > 0 && i % 100 == 0) {
                regIdNames(server, message);
                message = new SCIdNameMessage();
            }
            message.getIdNames().add(idNames.get(i));
        }
        if (message.getIdNames().size() > 0) {
            regIdNames(server, message);
        }
    }

    private void regIdNames(RealityServer server, SCIdNameMessage message) {
        Server2GatewayMessage gatewayMessage = new Server2GatewayMessage();
        gatewayMessage.setMessageId(message.getMessageId());
        gatewayMessage.setMessage(message);
        gatewayMessage.setUserIds(new Long[]{0L});
        server.getChannel().writeAndFlush(gatewayMessage);
    }
}
