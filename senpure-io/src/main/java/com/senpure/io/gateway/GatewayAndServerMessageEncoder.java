package com.senpure.io.gateway;

import com.senpure.io.message.Client2GatewayMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将客户端发到网关的消息，重新编码，发送给具体的服务器
 */
public class GatewayAndServerMessageEncoder extends MessageToByteEncoder<Client2GatewayMessage> {
    protected Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    protected void encode(ChannelHandlerContext ctx, Client2GatewayMessage msg, ByteBuf out) throws Exception {

        //head 4 messageId 4 token +8 + playerId 8+ data
        out.ensureWritable(24 + msg.getData().length);
        out.writeInt(20 + msg.getData().length);
        out.writeInt(msg.getMessageId());
        out.writeLong(msg.getToken());
        out.writeLong(msg.getUserId());
        out.writeBytes(msg.getData());


    }
}
