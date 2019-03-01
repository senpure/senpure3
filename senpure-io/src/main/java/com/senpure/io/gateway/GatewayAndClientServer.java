package com.senpure.io.gateway;

import com.senpure.base.util.Assert;
import com.senpure.io.ServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GatewayAndClientServer {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ChannelFuture channelFuture;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private String readableName = "网关服务器[CS]";
    private GatewayMessageExecuter messageExecuter;
    private ServerProperties.Gateway properties;

    public boolean start() {
        Assert.notNull(messageExecuter);
        Assert.notNull(properties);
        logger.info("启动{} CS模块，监听端口号 {}", properties.getReadableName(), properties.getCsPort());
        readableName = properties.getReadableName() + "[CS][" + properties.getCsPort() + "]";
        // Configure SSL.
        SslContext sslCtx = null;
        if (properties.isCsSsl()) {
            try {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } catch (Exception e) {
                logger.error("使用ssl出错", e);
            }
        } else {
            sslCtx = null;
        }

        try {
            // Configure the server.
            bossGroup = new NioEventLoopGroup(properties.getIoCsBossThreadPoolSize());
            workerGroup = new NioEventLoopGroup(properties.getIoCsWorkThreadPoolSize());
            ServerBootstrap b = new ServerBootstrap();
            SslContext finalSslCtx = sslCtx;
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (finalSslCtx != null) {
                                p.addLast(finalSslCtx.newHandler(ch.alloc()));
                            }
                            p.addLast(new GatewayAndClientMessageDecoder());
                            p.addLast(new GatewayAndClientMessageEncoder());
                            p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            p.addLast(new GatewayAndClientServerHandler(messageExecuter));

                        }
                    });
            // Start the server.
            channelFuture = b.bind(properties.getCsPort()).sync();
            logger.info("{}启动完成", getReadableName());
        } catch (Exception e) {
            logger.error("启动" + getReadableName() + " 失败", e);
            destroy();
            return false;
        }
        return true;
    }


    private  String getReadableName() {
        return readableName;
    }


    public void setMessageExecuter(GatewayMessageExecuter messageExecuter) {
        this.messageExecuter = messageExecuter;
    }

    public void setProperties(ServerProperties.Gateway properties) {
        this.properties = properties;
    }

    public void destroy() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        logger.debug("关闭{}并释放资源 ", readableName);

    }


}
