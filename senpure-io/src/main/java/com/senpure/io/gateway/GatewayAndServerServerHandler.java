package com.senpure.io.gateway;


import com.senpure.io.ChannelAttributeUtil;
import com.senpure.io.message.Server2GatewayMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GatewayAndServerServerHandler extends SimpleChannelInboundHandler<Server2GatewayMessage> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private GatewayMessageExecuter messageExecuter;


    public GatewayAndServerServerHandler(GatewayMessageExecuter messageExecuter) {
        this.messageExecuter = messageExecuter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Server2GatewayMessage msg) throws Exception {
        //  messageExecuter.execute(msg);
        messageExecuter.execute(ctx.channel(), msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String serverName = ChannelAttributeUtil.getServerName(channel);
        logger.debug("{} {} {} 断开连接", serverName, ChannelAttributeUtil.getServerKey(channel), channel);
        if (serverName != null) {
            ServerManager serverManager = messageExecuter.serverInstanceMap.get(serverName);
            messageExecuter.execute(() -> serverManager.serverOffLine(channel));

        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        super.exceptionCaught(ctx, cause);
    }
}
