package com.senpure.io;

import com.senpure.io.bean.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ServerHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {


        MessageHandlerUtil.execute(ctx.channel(),message);
    }

}
