package com.senpure.io.gateway;


import com.senpure.io.ChannelAttributeUtil;
import com.senpure.io.message.Client2GatewayMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GatewayAndClientServerHandler extends SimpleChannelInboundHandler<Client2GatewayMessage> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private GatewayMessageExecuter messageExecuter;


    public GatewayAndClientServerHandler(GatewayMessageExecuter messageExecuter) {
        this.messageExecuter = messageExecuter;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        messageExecuter.channelActive(ctx.channel());
        //super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Client2GatewayMessage msg) throws Exception {
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
        logger.debug("客户端{} 断开连接", channel);
        messageExecuter.clientOffline(channel);

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            Channel channel = ctx.channel();
            logger.info("客户端{} :{}  :{} 心跳失败", channel, ChannelAttributeUtil.getToken(channel), ChannelAttributeUtil.getUserId(channel));
            ctx.close();
            return;
        }
        super.userEventTriggered(ctx, evt);
    }
}
