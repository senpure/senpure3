package com.senpure.io.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RealityServerUtil
 *
 * @author senpure
 * @time 2019-03-05 09:22:51
 */
public class RealityServerUtil {

    private Map<String, Boolean> connectedGatewayMap = new ConcurrentHashMap();

    private Map<String, Boolean> prepareConnectGatewayMap = new ConcurrentHashMap();

    public static String getServerKey(String host, int port) {
        return host + ":" + port;
    }

    public void prepareConnectGateway(String host, int port) {
        prepareConnectGatewayMap.putIfAbsent(getServerKey(host, port), true);
    }

    public void connectGatewayFail(String host, int port) {
        prepareConnectGatewayMap.remove(getServerKey(host, port));
    }
}
