package com.senpure.io;


import com.senpure.io.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ServerHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {


        MessageHandlerUtil.execute(ctx.channel(), message);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //解决强断的错误 远程主机强迫关闭了一个现有的连接
        ctx.flush();
    }
}
