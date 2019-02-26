package com.senpure.io.server;


import com.senpure.io.message.Gateway2ServerMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class RealityServerHandler extends SimpleChannelInboundHandler<Gateway2ServerMessage> {


    private RealityMessageExecuter messageExecuter;

    public RealityServerHandler(RealityMessageExecuter messageExecuter) {
        this.messageExecuter = messageExecuter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Gateway2ServerMessage msg) throws Exception {
        messageExecuter.execute(ctx.channel(), msg);
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //解决强断的错误 远程主机强迫关闭了一个现有的连接
        ctx.flush();
    }
}
