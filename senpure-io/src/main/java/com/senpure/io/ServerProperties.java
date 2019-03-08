package com.senpure.io;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ServerProperties
 *
 * @author senpure
 * @time 2019-02-28 15:51:10
 */
@ConfigurationProperties("server.io")
public class ServerProperties {
    @Value("${spring.application.name:}")
    private String name;
    private Gateway gateway = new Gateway();
    private Producer producer = new Producer();

    public static class Producer {
        private String gatewayName = "gateway";
        private boolean setReadableName = false;
        private String readableName = "realityServer";
        private String idNamesPackage;
        private boolean enableEvent = true;
        private int eventThreadPoolSize = 0;
        private int ioWorkThreadPoolSize = 0;
        private int executerThreadPoolSize = 0;
        private long connectFailInterval = 20000;
        private int gatewayChannel = 2;
        private boolean outFormat = true;
        private boolean inFormat = true;
        private boolean ssl = false;
        private long writerIdleTime = 5000;


        public boolean isSetReadableName() {
            return setReadableName;
        }


        public String getReadableName() {
            return readableName;
        }

        public void setReadableName(String readableName) {
            this.readableName = readableName;
            setReadableName = true;
        }

        public int getExecuterThreadPoolSize() {
            return executerThreadPoolSize;
        }

        public void setExecuterThreadPoolSize(int executerThreadPoolSize) {
            this.executerThreadPoolSize = executerThreadPoolSize;
        }

        public long getConnectFailInterval() {
            return connectFailInterval;
        }

        public void setConnectFailInterval(long connectFailInterval) {
            this.connectFailInterval = connectFailInterval;
        }

        public int getIoWorkThreadPoolSize() {
            return ioWorkThreadPoolSize;
        }

        public void setIoWorkThreadPoolSize(int ioWorkThreadPoolSize) {
            this.ioWorkThreadPoolSize = ioWorkThreadPoolSize;
        }

        public long getWriterIdleTime() {
            return writerIdleTime;
        }

        public void setWriterIdleTime(long writerIdleTime) {
            this.writerIdleTime = writerIdleTime;
        }

        public String getGatewayName() {
            return gatewayName;
        }

        public void setGatewayName(String gatewayName) {
            this.gatewayName = gatewayName;
        }

        public boolean isEnableEvent() {
            return enableEvent;
        }

        public void setEnableEvent(boolean enableEvent) {
            this.enableEvent = enableEvent;
        }

        public int getEventThreadPoolSize() {
            return eventThreadPoolSize;
        }

        public void setEventThreadPoolSize(int eventThreadPoolSize) {
            this.eventThreadPoolSize = eventThreadPoolSize;
        }

        public int getGatewayChannel() {
            return gatewayChannel;
        }

        public void setGatewayChannel(int gatewayChannel) {
            this.gatewayChannel = gatewayChannel;
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

        public boolean isSsl() {
            return ssl;
        }

        public void setSsl(boolean ssl) {
            this.ssl = ssl;
        }

        public String getIdNamesPackage() {
            return idNamesPackage;
        }

        public void setIdNamesPackage(String idNamesPackage) {
            this.idNamesPackage = idNamesPackage;
        }
    }

    public static class Gateway {

        private boolean setReadableName = false;
        private String readableName = "gateway";
        private int executerThreadPoolSize = 0;
        private int ioCsBossThreadPoolSize = 1;
        private int ioScBossThreadPoolSize = 1;
        private int ioCsWorkThreadPoolSize = 0;
        private int ioScWorkThreadPoolSize = 0;
        private boolean csSsl = false;
        private boolean scSsl = false;
        private int csPort = 2222;
        private int scPort = 3333;

        private int csLoginMessageId = 1000100;
        private int scLoginMessageId = 1000101;
        private long csReaderIdleTime = 10000;
        private long scReaderIdleTime = 10000;

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


        public int getExecuterThreadPoolSize() {
            return executerThreadPoolSize;
        }

        public void setExecuterThreadPoolSize(int executerThreadPoolSize) {
            this.executerThreadPoolSize = executerThreadPoolSize;
        }


        public boolean isSetReadableName() {
            return setReadableName;
        }

        public String getReadableName() {
            return readableName;
        }

        public void setReadableName(String readableName) {
            this.readableName = readableName;
            setReadableName = true;
        }

        public boolean isCsSsl() {
            return csSsl;
        }

        public void setCsSsl(boolean csSsl) {
            this.csSsl = csSsl;
        }

        public boolean isScSsl() {
            return scSsl;
        }

        public void setScSsl(boolean scSsl) {
            this.scSsl = scSsl;
        }

        public int getIoCsBossThreadPoolSize() {
            return ioCsBossThreadPoolSize;
        }

        public void setIoCsBossThreadPoolSize(int ioCsBossThreadPoolSize) {
            this.ioCsBossThreadPoolSize = ioCsBossThreadPoolSize;
        }

        public int getIoCsWorkThreadPoolSize() {
            return ioCsWorkThreadPoolSize;
        }

        public void setIoCsWorkThreadPoolSize(int ioCsWorkThreadPoolSize) {
            this.ioCsWorkThreadPoolSize = ioCsWorkThreadPoolSize;
        }

        public int getIoScBossThreadPoolSize() {
            return ioScBossThreadPoolSize;
        }

        public void setIoScBossThreadPoolSize(int ioScBossThreadPoolSize) {
            this.ioScBossThreadPoolSize = ioScBossThreadPoolSize;
        }

        public int getIoScWorkThreadPoolSize() {
            return ioScWorkThreadPoolSize;
        }

        public void setIoScWorkThreadPoolSize(int ioScWorkThreadPoolSize) {
            this.ioScWorkThreadPoolSize = ioScWorkThreadPoolSize;
        }

        public long getCsReaderIdleTime() {
            return csReaderIdleTime;
        }

        public void setCsReaderIdleTime(long csReaderIdleTime) {
            this.csReaderIdleTime = csReaderIdleTime;
        }

        public long getScReaderIdleTime() {
            return scReaderIdleTime;
        }

        public void setScReaderIdleTime(long scReaderIdleTime) {
            this.scReaderIdleTime = scReaderIdleTime;
        }

        @Override
        public String toString() {
            return "Gateway{" +
                    ", readableName='" + readableName + '\'' +
                    ", executerThreadPoolSize=" + executerThreadPoolSize +
                    ", ioCsBossThreadPoolSize=" + ioCsBossThreadPoolSize +
                    ", ioScBossThreadPoolSize=" + ioScBossThreadPoolSize +
                    ", ioCsWorkThreadPoolSize=" + ioCsWorkThreadPoolSize +
                    ", ioScWorkThreadPoolSize=" + ioScWorkThreadPoolSize +
                    ", csSsl=" + csSsl +
                    ", ScSsl=" + scSsl +
                    ", csPort=" + csPort +
                    ", scPort=" + scPort +
                    ", csLoginMessageId=" + csLoginMessageId +
                    ", scLoginMessageId=" + scLoginMessageId +
                    '}';
        }
    }


    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
