package com.senpure.io;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;


public class ClientServer {
    protected Logger logger = LoggerFactory.getLogger(getClass());


    private IOServerProperties properties;
    private IOMessageProperties ioMessageProperties;
    private ChannelFuture channelFuture;
    private String serverName = "游戏客户端";
    private Channel channel;
    private EventLoopGroup group;
    private Bootstrap bootstrap;

    public Channel connect() throws CertificateException, SSLException {
        if (channel != null && channel.isActive()) {
            return channel;
        }
        if (properties == null) {
            properties = new IOServerProperties();
        }
        if (ioMessageProperties == null) {
            ioMessageProperties = new IOMessageProperties();
            ioMessageProperties.setInFormat(properties.isInFormat());
            ioMessageProperties.setOutFormat(properties.isOutFormat());
        }

        String host = properties.getHost();
        int port = properties.getPort();
        logger.debug("启动{}，服务器地址 {}", getServerName(), host + ":" + port);
        serverName = serverName + "[" + host + ":" + port + "]";
        // Configure SSL.
        final SslContext sslCtx;
        if (properties.isSsl()) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        if (bootstrap == null) {
            synchronized (this) {
                if (bootstrap == null) {
                    group = new NioEventLoopGroup(1);
                    bootstrap = new Bootstrap();
                    bootstrap.group(group)
                            .channel(NioSocketChannel.class)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline p = ch.pipeline();
                                    if (sslCtx != null) {
                                        p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                                    }
                                    p.addLast(new ByteBufToMessageDecoder());
                                    p.addLast(new MessageToByteBufEncoder());
                                    OffLineHandler offLineHandler = new OffLineHandler();
                                    ChannelAttributeUtil.setOfflineHandler(ch, offLineHandler);
                                    p.addLast(offLineHandler);
                                    if (properties.isEnableHeartCheck()) {
                                        p.addLast(new IdleStateHandler(0, properties.getWriterIdleTime(), 0, TimeUnit.MILLISECONDS));
                                    }
                                    p.addLast(new MessageLoggingHandler(LogLevel.DEBUG, ioMessageProperties));
                                    p.addLast(new ClientHandler());
                                }
                            });
                }
            }
        }

        // Start the client.
        try {
            logger.info("{}启动完成", getServerName());
            channelFuture = bootstrap.connect(host, port).sync();
            channel = channelFuture.channel();
            return channel;
        } catch (Exception e) {
            logger.error("启动" + getServerName() + " 失败", e);
            destroy();
            return null;
        }

    }

    public void destroy() {
//        if (channelFuture != null) {
//            channelFuture.channel().close();
//        }
        if (group != null) {
            group.shutdownGracefully();
        }
        logger.debug("关闭{}并释放资源 ", getServerName());

    }

    /**
     * 最近的一次连接
     *
     * @return
     */
    public Channel getChannel() {
        return channel;
    }

    public String getServerName() {
        return serverName;
    }


    public IOServerProperties getProperties() {
        return properties;
    }

    public void setProperties(IOServerProperties properties) {
        this.properties = properties;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
