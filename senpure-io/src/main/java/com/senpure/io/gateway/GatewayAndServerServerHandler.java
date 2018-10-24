package com.senpure.io.gateway;



import com.senpure.io.message.Server2GatewayMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class GatewayAndServerServerHandler extends SimpleChannelInboundHandler<Server2GatewayMessage> {

    private GatewayMessageExecuter messageExecuter;


    public GatewayAndServerServerHandler (GatewayMessageExecuter messageExecuter) {
        this.messageExecuter = messageExecuter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Server2GatewayMessage msg) throws Exception {
      //  messageExecuter.execute(msg);
        messageExecuter.execute(ctx.channel(),msg);
    }

}
