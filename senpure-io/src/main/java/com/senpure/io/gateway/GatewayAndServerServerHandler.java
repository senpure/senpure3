package com.senpure.io.gateway;


import com.senpure.io.ChannelAttributeUtil;
import com.senpure.io.event.SourceOffline;
import com.senpure.io.message.Server2GatewayMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GatewayAndServerServerHandler extends SimpleChannelInboundHandler<Server2GatewayMessage> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private GatewayMessageExecuter messageExecuter;

    private SourceOffline sourceOffline;

    public GatewayAndServerServerHandler(GatewayMessageExecuter messageExecuter) {
        this.messageExecuter = messageExecuter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Server2GatewayMessage msg) throws Exception {
        //  messageExecuter.execute(msg);
        messageExecuter.execute(ctx.channel(), msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //解决强断的错误 远程主机强迫关闭了一个现有的连接
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String serverName = ChannelAttributeUtil.getRemoteServerName(channel);
        logger.debug("{} {} {} 断开连接", serverName, ChannelAttributeUtil.getRemoteServerKey(channel), channel);
        if (serverName != null) {
            ServerManager serverManager = messageExecuter.serverInstanceMap.get(serverName);
            messageExecuter.execute(() -> serverManager.serverOffLine(channel));
        }

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            Channel channel = ctx.channel();
            logger.info("服务器{} :{}  :{} 心跳失败", channel, ChannelAttributeUtil.getRemoteServerName(channel), ChannelAttributeUtil.getRemoteServerKey(channel));
            ctx.close();
            return;
        }
        super.userEventTriggered(ctx, evt);
    }
}
