package com.senpure.io.server;


import com.senpure.io.ChannelAttributeUtil;
import com.senpure.io.message.Gateway2ServerMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class RealityServerHandler extends SimpleChannelInboundHandler<Gateway2ServerMessage> {


    private RealityMessageExecuter messageExecuter;
    private GatewayManager gatewayManager;

    public RealityServerHandler(RealityMessageExecuter messageExecuter, GatewayManager gatewayManager) {
        this.messageExecuter = messageExecuter;
        this.gatewayManager = gatewayManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Gateway2ServerMessage msg) throws Exception {
        messageExecuter.execute(ctx.channel(), msg);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        gatewayManager.getGatewayChannelServer(ChannelAttributeUtil.getRemoteServerKey(ctx.channel())).removeChannel(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //解决强断的错误 远程主机强迫关闭了一个现有的连接
        ctx.flush();
    }
}
