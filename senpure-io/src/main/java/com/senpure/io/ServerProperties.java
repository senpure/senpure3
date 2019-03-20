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
        /**
         * 网关服务名
         */
        private String gatewayName = "gateway";
        private boolean setReadableName = false;
        /**
         * 服务器名
         */
        private String readableName = "realityServer";
        /**
         * 关联id与类名的包名
         */
        private String idNamesPackage;
        /**
         * 开启事件
         */
        private boolean enableEvent = true;
        /**
         * 处理事件的线程数
         */
        private int eventThreadPoolSize = 0;
        /**
         * netty workGroup 线程数
         */
        private int ioWorkThreadPoolSize = 0;
        /**
         * 逻辑处理线程数
         */
        private int executerThreadPoolSize = 0;
        /**
         * 连接网关失败后下一次连接间隔毫秒
         */
        private long connectFailInterval = 20000;
        /**
         * 与网关建立的channl 数量
         */
        private int gatewayChannel = 2;
        /**
         * 输出格式化
         */
        private boolean outFormat = true;
        /**
         * 输入格式化
         */
        private boolean inFormat = true;
        /**
         * 开启ssl
         */
        private boolean ssl = false;
        /**
         * 是否开启心跳检查
         */
        private boolean enableHeartCheck = false;
        /**
         * 心跳写入间隔毫秒
         */
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

        public boolean isEnableHeartCheck() {
            return enableHeartCheck;
        }

        public void setEnableHeartCheck(boolean enableHeartCheck) {
            this.enableHeartCheck = enableHeartCheck;
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
        /**
         * 服务名不是唯一
         */
        private String readableName = "gateway";
        /**
         * 工作现场数量
         */
        private int executerThreadPoolSize = 0;
        /**
         * cs模块 netty boosGroup 线程数
         */
        private int ioCsBossThreadPoolSize = 1;
        /**
         * sc模块 netty boosGroup 线程数
         */
        private int ioScBossThreadPoolSize = 1;
        /**
         * cs模块 netty workGroup 线程数
         */
        private int ioCsWorkThreadPoolSize = 0;
        /**
         * sc模块 netty workGroup 线程数
         */
        private int ioScWorkThreadPoolSize = 0;
        /**
         * cs 是否开启ssl
         */
        private boolean csSsl = false;
        /**
         * sc 是否开启ssl
         */
        private boolean scSsl = false;
        /**
         * cs 模块端口号
         */
        private int csPort = 2222;
        /**
         * sc 模块端口号
         */
        private int scPort = 3333;

        /**
         * 客户端登录消息id
         */
        private int csLoginMessageId = 1000100;
        /**
         * 服务器登录成功返回消息id
         */
        private int scLoginMessageId = 1000101;
        /**
         * 是否开启cs 模块的心跳检查
         */
        private boolean enableCSHeartCheck = true;
        /**
         * cs模块的的读心跳时间毫秒
         */
        private long csReaderIdleTime = 10000;
        /**
         *  是否开启sc 模块的心跳检查
         */
        private boolean enableSCHeartCheck = false;
        /**
         * sc模块的的心跳读时间毫秒
         */
        private long scReaderIdleTime = 10000;
        /**
         * 雪花算法的服务名
         */
        private String snowflakeDispatcherName = "snowflake-dispatcher";
        /**
         * 是否直接是配置文件中的雪花算法的dataCenterId 与workId
         */
        private boolean snowflakeUseCode = false;
        /**
         * 雪花算法 dataCenterId
         */
        private int snowflakeDataCenterId = 0;
        /**
         * 雪花算法 workId
         */
        private int snowflakeworkId = 0;
        /**
         * 询问处理最多延迟毫秒
         */
        private long askMaxDelay=3000;

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


        public long getAskMaxDelay() {
            return askMaxDelay;
        }

        public void setAskMaxDelay(long askMaxDelay) {
            this.askMaxDelay = askMaxDelay;
        }

        public int getExecuterThreadPoolSize() {
            return executerThreadPoolSize;
        }

        public void setExecuterThreadPoolSize(int executerThreadPoolSize) {
            this.executerThreadPoolSize = executerThreadPoolSize;
        }

        public String getSnowflakeDispatcherName() {
            return snowflakeDispatcherName;
        }

        public boolean isSnowflakeUseCode() {
            return snowflakeUseCode;
        }

        public void setSnowflakeUseCode(boolean snowflakeUseCode) {
            this.snowflakeUseCode = snowflakeUseCode;
        }

        public int getSnowflakeDataCenterId() {
            return snowflakeDataCenterId;
        }

        public void setSnowflakeDataCenterId(int snowflakeDataCenterId) {
            this.snowflakeDataCenterId = snowflakeDataCenterId;
        }

        public int getSnowflakeworkId() {
            return snowflakeworkId;
        }

        public void setSnowflakeworkId(int snowflakeworkId) {
            this.snowflakeworkId = snowflakeworkId;
        }

        public void setSnowflakeDispatcherName(String snowflakeDispatcherName) {
            this.snowflakeDispatcherName = snowflakeDispatcherName;
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

        public boolean isEnableSCHeartCheck() {
            return enableSCHeartCheck;
        }

        public void setEnableSCHeartCheck(boolean enableSCHeartCheck) {
            this.enableSCHeartCheck = enableSCHeartCheck;
        }

        public boolean isEnableCSHeartCheck() {
            return enableCSHeartCheck;
        }

        public void setEnableCSHeartCheck(boolean enableCSHeartCheck) {
            this.enableCSHeartCheck = enableCSHeartCheck;
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
