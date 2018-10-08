package com.senpure.io.gateway;


import com.senpure.io.message.Client2GatewayMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class GatewayAndClientServerHandler extends SimpleChannelInboundHandler<Client2GatewayMessage> {


    private MessageExecuter messageExecuter;


    public GatewayAndClientServerHandler(MessageExecuter messageExecuter) {
        this.messageExecuter = messageExecuter;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        messageExecuter.channelActive(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Client2GatewayMessage msg) throws Exception {
        messageExecuter.execute(ctx.channel(), msg);
    }

}
