package com.senpure.io;


import com.senpure.io.message.CSHeartMessage;
import com.senpure.io.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientHandler extends SimpleChannelInboundHandler<Message> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        MessageHandlerUtil.execute(ctx.channel(), message);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //解决强断的错误 远程主机强迫关闭了一个现有的连接
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            logger.info("{}客户端心跳检查",ctx.channel());
            CSHeartMessage csHeartMessage = new CSHeartMessage();
            ctx.channel().writeAndFlush(csHeartMessage);
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }
}
