package com.senpure.io.support.configure;

import com.senpure.base.AppEvn;
import com.senpure.base.util.Assert;
import com.senpure.io.ServerProperties;
import com.senpure.io.support.GatewayServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GatewayAutoConfiguration
 * <b>自动获取雪花数据时注意不在一个数据中心的机器，可能出现serverKey重复的情况 (本机ip地址相同)</b>
 *
 * @author senpure
 * @time 2019-02-28 16:52:21
 */
@ConditionalOnClass({CompositeDiscoveryClientAutoConfiguration.class, RestTemplateAutoConfiguration.class, LoadBalancerAutoConfiguration.class})
@AutoConfigureAfter({CompositeDiscoveryClientAutoConfiguration.class, RestTemplateAutoConfiguration.class, LoadBalancerAutoConfiguration.class})
public class GatewayAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(getClass());


    public GatewayAutoConfiguration(RestTemplateBuilder restTemplateBuilder, List<RestTemplateCustomizer> restTemplateCustomizers) {
        restTemplate = restTemplateBuilder.build();

        // logger.info("======restTemplateCustomizers{}",restTemplateCustomizers);
        for (RestTemplateCustomizer customizer : restTemplateCustomizers) {
            customizer.customize(restTemplate);
        }
    }

    @Autowired
    private ServerProperties properties;
    @Autowired
    private DiscoveryClient discoveryClient;

    // @Autowired
    // private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    private RestTemplate restTemplate;


    @Bean

    public GatewayServer gatewayServer() {

        if (properties.getGateway().isSnowflakeUseCode()) {

        } else {
            ServiceInstance serviceInstance = loadBalancerClient.choose(properties.getGateway().getSnowflakeDispatcherName());
            if (serviceInstance == null) {
                // logger.error("{} 雪花调度服务没有启动", properties.getGateway().getSnowflakeDispatcherName());
                Assert.error(properties.getGateway().getSnowflakeDispatcherName() + "雪花调度服务没有启动");
            }
            String url = "http://" + properties.getGateway().getSnowflakeDispatcherName() + "/snowflake/dispatch?serverKey={serverKey}";
            Map<String, String> params = new LinkedHashMap<>();
            params.put("serverName", properties.getName());
            String serverKey;
            if (AppEvn.classInJar(AppEvn.getStartClass())) {
                serverKey = AppEvn.getClassPath(AppEvn.getStartClass());
            } else {
                serverKey = AppEvn.getClassRootPath();
            }
            serverKey = getLocalHostLANAddress().getHostAddress() + "->" + serverKey;
            params.put("serverKey", serverKey);
            //  ObjectNode nodes = restTemplate.getForObject(url, ObjectNode.class, params);
            // logger.debug("雪花调度返回 {}", JSON.toJSONString(nodes));
            Result result = restTemplate.getForObject(url, Result.class, params);

            logger.debug("雪花调度返回 {}", result);
            if (result.getCode() != 1) {
                Assert.error(properties.getGateway().getSnowflakeDispatcherName() + "雪花调度服务出错 :" + result.message + (result.getValidators() == null ? "" : result.getValidators().toString()));
            }
            properties.getGateway().setSnowflakeDataCenterId(result.getServerCenterAndWork().getCenterId());
            properties.getGateway().setSnowflakeworkId(result.getServerCenterAndWork().getWorkId());
        }

        GatewayServer gatewayServer = new GatewayServer();
        gatewayServer.setProperties(properties);
        gatewayServer.start();
        return gatewayServer;
    }

    static class Result {
        private int code;
        private String message;
        private ServerCenterAndWork serverCenterAndWork;
        private Map<String, String> validators;

        public Map<String, String> getValidators() {
            return validators;
        }

        public void setValidators(Map<String, String> validators) {
            this.validators = validators;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ServerCenterAndWork getServerCenterAndWork() {
            return serverCenterAndWork;
        }

        public void setServerCenterAndWork(ServerCenterAndWork serverCenterAndWork) {
            this.serverCenterAndWork = serverCenterAndWork;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", serverCenterAndWork=" + serverCenterAndWork +
                    ", validators=" + validators +
                    '}';
        }
    }

    static class ServerCenterAndWork {
        private String serverName;

        private String serverKey;

        private Integer centerId;

        private Integer workId;

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getServerKey() {
            return serverKey;
        }

        public void setServerKey(String serverKey) {
            this.serverKey = serverKey;
        }

        public Integer getCenterId() {
            return centerId;
        }

        public void setCenterId(Integer centerId) {
            this.centerId = centerId;
        }

        public Integer getWorkId() {
            return workId;
        }

        public void setWorkId(Integer workId) {
            this.workId = workId;
        }

        @Override
        public String toString() {
            return "ServerCenterAndWork{" +
                    "serverName='" + serverName + '\'' +
                    ", serverKey='" + serverKey + '\'' +
                    ", centerId=" + centerId +
                    ", workId=" + workId +
                    '}';
        }
    }

    public static InetAddress getLocalHostLANAddress() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            java.net.InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return jdkSuppliedAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws UnknownHostException {
        String str = " {\"code\":104,\"validators\":{\"serverName\":\"不能为空\"},\"message\":\"输入格式错误\"}";


        System.out.println(getLocalHostLANAddress().getHostAddress());
        java.net.InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
        System.out.println(jdkSuppliedAddress);
    }
}
