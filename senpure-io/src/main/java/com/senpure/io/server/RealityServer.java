package com.senpure.io.server;

import com.senpure.base.AppEvn;
import com.senpure.base.util.Assert;
import com.senpure.io.ChannelAttributeUtil;
import com.senpure.io.IOMessageProperties;
import com.senpure.io.ServerProperties;
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

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


public class RealityServer {
    protected static Logger logger = LoggerFactory.getLogger(RealityServer.class);
    private ServerProperties.Producer properties;
    private IOMessageProperties ioMessageProperties;
    private ChannelFuture channelFuture;
    private String serverName = "realityServer";
    private String readableServerName = "realityServer";
    private boolean setReadableServerName = false;
    private RealityMessageExecuter messageExecuter;

    private Channel channel;
    private GatewayManager gatewayManager;


    private static EventLoopGroup group;
    private static Bootstrap bootstrap;
    private static final Object groupLock = new Object();

    private static int serverRefCont = 0;

    public final boolean start(String host, int port) {
        Assert.notNull(gatewayManager);
        Assert.notNull(properties);
        Assert.notNull(messageExecuter);
        if (ioMessageProperties == null) {
            ioMessageProperties = new IOMessageProperties();
            ioMessageProperties.setInFormat(properties.isInFormat());
            ioMessageProperties.setOutFormat(properties.isOutFormat());
        }
        // Configure SSL.
        if (group == null || group.isShuttingDown() || group.isShutdown()) {
            synchronized (groupLock) {
                if (group == null || group.isShuttingDown() || group.isShutdown()) {
                    group = new NioEventLoopGroup();
                    SslContext sslCtx = null;
                    try {
                        if (properties.isSsl()) {
                            SelfSignedCertificate ssc = new SelfSignedCertificate();
                            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
                        }
                    } catch (Exception e) {
                        logger.error("使用ssl出错", e);
                    }
                    bootstrap = new Bootstrap();
                    SslContext finalSslCtx = sslCtx;
                    bootstrap.group(group)
                            .channel(NioSocketChannel.class)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) {
                                    ChannelPipeline p = ch.pipeline();
                                    if (finalSslCtx != null) {
                                        p.addLast(finalSslCtx.newHandler(ch.alloc(), host, port));
                                    }
                                    p.addLast(new RealityMessageDecoder());
                                    p.addLast(new RealityMessageEncoder());
                                    p.addLast(new RealityMessageLoggingHandler(LogLevel.DEBUG, ioMessageProperties));
                                    p.addLast(new IdleStateHandler(0, properties.getWriterIdleTime(), 0, TimeUnit.MILLISECONDS));
                                    p.addLast(new RealityServerHandler(messageExecuter, gatewayManager));
                                }
                            });

                }
            }
        }
        // Start the client.
        try {
            logger.debug("启动{}，网关地址 {}", properties.getReadableName(), host + ":" + port);
            readableServerName = properties.getReadableName() + "->[" + host + ":" + port + "]";
            channelFuture = bootstrap.connect(host, port).sync();
            channel = channelFuture.channel();
            synchronized (groupLock) {
                serverRefCont++;
            }
            InetSocketAddress address = (InetSocketAddress) channel.localAddress();

            String gatewayKey = gatewayManager.getGatewayKey(host, port);
            String path;
            if (AppEvn.classInJar(AppEvn.getStartClass())) {
                path = AppEvn.getClassPath(AppEvn.getStartClass());
            } else {
                path = AppEvn.getClassRootPath();
            }
            String serverKey = address.getAddress().getHostAddress() + "->" + path;
            GatewayChannelManager channelServer = gatewayManager.getGatewayChannelServer(gatewayKey);
            channelServer.addChannel(channel);
            ChannelAttributeUtil.setRemoteServerKey(channel, gatewayKey);
            ChannelAttributeUtil.setLocalServerKey(channel, serverKey);
            logger.info("{}启动完成 localServerKey {} address {}", getReadableServerName(), serverKey, address);
        } catch (Exception e) {
            logger.error("启动" + getReadableServerName() + " 失败", e);
            destroy();
            return false;
        }
        return true;

    }


    public void setProperties(ServerProperties.Producer properties) {
        this.properties = properties;
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
                if (group != null && (!group.isShutdown() | !group.isShuttingDown())) {
                    logger.debug("{} 关闭 group 并释放资源 ", readableServerName);
                    group.shutdownGracefully();
                }
            }

        }


    }


    public void setGatewayManager(GatewayManager gatewayManager) {
        this.gatewayManager = gatewayManager;
    }


    public String getServerName() {
        return serverName;
    }


    public void setMessageExecuter(RealityMessageExecuter messageExecuter) {
        this.messageExecuter = messageExecuter;
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

    public static void main(String[] args) {

        InetSocketAddress address = new InetSocketAddress(8111);

        System.out.println(address.getAddress().getCanonicalHostName());
    }
}
