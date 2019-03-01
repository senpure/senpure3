package com.senpure.io;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("server.io")
public class IOServerProperties {


    private boolean ssl = false;
    private int port = 1111;
    private String host = "127.0.0.1";
    private boolean inFormat = false;
    private boolean outFormat = false;
    private String gatewayAddress = "127.0.0.1:3333";

    private int csPort = 2222;
    private int scPort = 3333;

    private int csLoginMessageId = 100010;
    private int scLoginMessageId = 100011;

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isInFormat() {
        return inFormat;
    }

    public void setInFormat(boolean inFormat) {
        this.inFormat = inFormat;
    }

    public boolean isOutFormat() {
        return outFormat;
    }

    public void setOutFormat(boolean outFormat) {
        this.outFormat = outFormat;
    }


    public int getCsPort() {
        return csPort;
    }

    public void setCsPort(int csPort) {
        this.csPort = csPort;
    }

    public int getScPort() {
        return scPort;
    }

    public void setScPort(int scPort) {
        this.scPort = scPort;
    }

    public String getGatewayAddress() {
        return gatewayAddress;
    }

    public void setGatewayAddress(String gatewayAddress) {
        this.gatewayAddress = gatewayAddress;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getCsLoginMessageId() {
        return csLoginMessageId;
    }

    public void setCsLoginMessageId(int csLoginMessageId) {
        this.csLoginMessageId = csLoginMessageId;
    }

    public int getScLoginMessageId() {
        return scLoginMessageId;
    }

    public void setScLoginMessageId(int scLoginMessageId) {
        this.scLoginMessageId = scLoginMessageId;
    }
}
