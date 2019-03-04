package com.senpure.io;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ServerProperties
 *
 * @author senpure
 * @time 2019-02-28 15:51:10
 */
@ConfigurationProperties("server.io")
public class ServerProperties {

    private Gateway gateway = new Gateway();
    private Producer producer = new Producer();

    public static class Producer {
        private String gatewayName = "gateway";
        private boolean enableEvent=true;
        private int eventThreadPoolSize=0;


        public String getGatewayName() {
            return gatewayName;
        }

        public void setGatewayName(String gatewayName) {
            this.gatewayName = gatewayName;
        }
    }

    public static class Gateway {

        private String name = "gateway";
        private String readableName = "网关服务器";
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


        public String getReadableName() {
            return readableName;
        }

        public void setReadableName(String readableName) {
            this.readableName = readableName;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        @Override
        public String toString() {
            return "Gateway{" +
                    "name='" + name + '\'' +
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
}
