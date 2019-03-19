package com.senpure.io;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("io")
public class IOServerProperties {

    private int port = 2222;
    private String host = "127.0.0.1";
    private boolean ssl;
    private boolean outFormat = true;
    private boolean inFormat = true;
    private long writerIdleTime = 5000;
    private long readIdleTime = 5000;

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
}
