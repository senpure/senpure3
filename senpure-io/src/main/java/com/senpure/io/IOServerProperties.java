package com.senpure.io;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("io")
public class IOServerProperties {
    /**
     * 服务端口号
     */
    private int port = 2222;
    /**
     * io服务地址
     */
    private String host = "127.0.0.1";
    /**
     * 开启ssl
     */
    private boolean ssl;
    /**
     * 输出是否格式化
     */
    private boolean outFormat = true;
    /**
     * 输入是否格式化
     */
    private boolean inFormat = true;
    /**
     * 写心跳检查时间毫秒
     */
    private long writerIdleTime = 5000;
    /**
     * 读心跳检查时间毫秒
     */
    private long readIdleTime = 5000;
    private boolean enableHeartCheck=true;

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public boolean isOutFormat() {
        return outFormat;
    }

    public void setOutFormat(boolean outFormat) {
        this.outFormat = outFormat;
    }

    public boolean isInFormat() {
        return inFormat;
    }

    public void setInFormat(boolean inFormat) {
        this.inFormat = inFormat;
    }

    public long getWriterIdleTime() {
        return writerIdleTime;
    }

    public void setWriterIdleTime(long writerIdleTime) {
        this.writerIdleTime = writerIdleTime;
    }

    public long getReadIdleTime() {
        return readIdleTime;
    }

    public void setReadIdleTime(long readIdleTime) {
        this.readIdleTime = readIdleTime;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isEnableHeartCheck() {
        return enableHeartCheck;
    }

    public void setEnableHeartCheck(boolean enableHeartCheck) {
        this.enableHeartCheck = enableHeartCheck;
    }
}
