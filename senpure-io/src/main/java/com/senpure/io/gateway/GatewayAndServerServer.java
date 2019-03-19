package com.senpure.io.gateway;

import com.senpure.base.util.Assert;
import com.senpure.io.ServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class GatewayAndServerServer {
    protected Logger logger = LoggerFactory.getLogger(getClass());


    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private String readableServerName = "网关服务器[SC]";

    private GatewayMessageExecuter messageExecuter;
    private ServerProperties.Gateway properties;

    public boolean start() {
        Assert.notNull(messageExecuter);
        Assert.notNull(properties);
        logger.debug("启动 {} SC模块，监听端口号 {}", properties.getReadableName(), properties.getScPort());
        readableServerName = properties.getReadableName() + "[SC][" + properties.getScPort() + "]";
        SslContext sslCtx = null;
        // Configure SSL.
        if (properties.isScSsl()) {
            try {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } catch (Exception e) {
                logger.error("使用ssl出错", e);
            }
        }
        try {
            // Configure the server.
            bossGroup = new NioEventLoopGroup(properties.getIoScBossThreadPoolSize());
            workerGroup = new NioEventLoopGroup(properties.getIoScWorkThreadPoolSize());
            ServerBootstrap b = new ServerBootstrap();
            SslContext finalSslCtx = sslCtx;
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            if (finalSslCtx != null) {
                                p.addLast(finalSslCtx.newHandler(ch.alloc()));
                            }
                            p.addLast(new GatewayAndServerMessageDecoder());
                            p.addLast(new GatewayAndServerMessageEncoder());
                            p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            if (properties.isEnableSCHeartCheck()) {
                                p.addLast(new IdleStateHandler(properties.getScReaderIdleTime(), 0L, 0L, TimeUnit.MILLISECONDS));
                            }
                            p.addLast(new GatewayAndServerServerHandler(messageExecuter));

                        }
                    });
            // Start the server.
            b.bind(properties.getScPort()).sync();
            logger.info("{} 启动完成", getReadableServerName());
        } catch (Exception e) {
            logger.error("启动 " + getReadableServerName() + " 失败", e);
            destroy();
            return false;
        }

        return true;
    }


    public void destroy() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        logger.info("关闭{}并释放资源 ", getReadableServerName());

    }

    public String getReadableServerName() {
        return readableServerName;
    }


    public void setMessageExecuter(GatewayMessageExecuter messageExecuter) {
        this.messageExecuter = messageExecuter;
    }

    public void setProperties(ServerProperties.Gateway properties) {
        this.properties = properties;
    }


}
