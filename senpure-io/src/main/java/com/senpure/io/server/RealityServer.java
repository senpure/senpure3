package com.senpure.io.server;

import com.senpure.base.util.Assert;
import com.senpure.io.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RealityServer {
    protected static Logger logger = LoggerFactory.getLogger(RealityServer.class);
    private IOServerProperties properties;
    private IOMessageProperties ioMessageProperties;
    private ChannelFuture channelFuture;
    private String serverName = "realityServer";
    private String readableServerName = "realityServer";
    private boolean setReadableServerName = false;
    private RealityMessageExecuter messageExecuter;

    private String host;
    private Channel channel;
    private GatewayManager gatewayManager;

    private static Map<String, Integer> firstPortMap = new ConcurrentHashMap<>();
    private static EventLoopGroup group;
    private static Bootstrap bootstrap;
    private static Object groupLock = new Object();

    private static int serverRefCont = 0;

    public final boolean start(String host, int port) throws CertificateException, SSLException {
        Assert.notNull(gatewayManager);
        Assert.notNull(messageExecuter);
        this.host = host;
        if (properties == null) {
            properties = new IOServerProperties();
        }
        if (ioMessageProperties == null) {
            ioMessageProperties = new IOMessageProperties();
            ioMessageProperties.setInFormat(properties.isInFormat());
            ioMessageProperties.setOutFormat(properties.isOutFormat());
        }
        // Configure SSL.
        if (group == null) {
            synchronized (groupLock) {
                if (group == null) {
                    group = new NioEventLoopGroup();
                    final SslContext sslCtx;
                    if (properties.isSsl()) {
                        SelfSignedCertificate ssc = new SelfSignedCertificate();
                        sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
                    } else {
                        sslCtx = null;
                    }
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
                                    p.addLast(new RealityMessageDecoder());
                                    p.addLast(new RealityMessageEncoder());
                                    p.addLast(new RealityMessageLoggingHandler(LogLevel.DEBUG, ioMessageProperties));
                                    p.addLast(new RealityServerHandler(messageExecuter));
                                }
                            });

                }
            }
        }
        // Start the client.
        try {
            logger.debug("启动{}，网关地址 {}", getReadableServerName(), host + ":" + port);
            if (!setReadableServerName) {
                readableServerName = readableServerName + "[" + host + ":" + port + "]";
            }
            channelFuture = bootstrap.connect(host, port).sync();
            channel = channelFuture.channel();
            synchronized (groupLock) {
                serverRefCont++;
            }
            InetSocketAddress address = (InetSocketAddress) channel.localAddress();
            logger.info("{}启动完成", getReadableServerName());
            markFirstPort(host, address.getPort());
            String gatewayKey = host + ":" + port;
            GatewayChannelManager channelServer = gatewayManager.getGatewayChannelServer(gatewayKey);
            channelServer.addChannel(channel);
            ChannelAttributeUtil.setIpAndPort(channel, gatewayKey);

        } catch (Exception e) {
            logger.error("启动" + getReadableServerName() + " 失败", e);
            destroy();
            return false;
        }
        return true;

    }


    public Channel getChannel() {
        return channel;
    }

    public String getReadableServerName() {
        return readableServerName;
    }

    public void destroy() {
        if (channelFuture != null) {
            channelFuture.channel().close();
            synchronized (groupLock) {
                serverRefCont--;
            }
        }
        logger.debug("关闭{}并释放资源 ", getReadableServerName());
        tryDestroyGroup(getReadableServerName());
    }

    private synchronized static void tryDestroyGroup(String readableServerName) {
        synchronized (groupLock) {
            if (serverRefCont == 0) {
                if (group != null&&(!group.isShutdown()|!group.isShuttingDown())) {
                    logger.debug("{} 关闭 group 并释放资源 ", readableServerName);
                    group.shutdownGracefully();
                }
            }

        }


    }

    private synchronized static void markFirstPort(String host, int port) {
        firstPortMap.putIfAbsent(host, port);

    }

    public void setGatewayManager(GatewayManager gatewayManager) {
        this.gatewayManager = gatewayManager;
    }

    public int getFirstPort() {
        return firstPortMap.get(host);
    }

    public String getServerName() {
        return serverName;
    }


    public void setMessageExecuter(RealityMessageExecuter messageExecuter) {
        this.messageExecuter = messageExecuter;
    }

    public IOServerProperties getProperties() {
        return properties;
    }

    public void setProperties(IOServerProperties properties) {
        this.properties = properties;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
        if (!setReadableServerName) {
            readableServerName = serverName;
        }
    }


    public void setReadableServerName(String readableServerName) {
        this.readableServerName = readableServerName;
        setReadableServerName = true;
    }
}
