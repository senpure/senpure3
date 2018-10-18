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

        //messageId 4 token +4 + playerId 4+ data
        out.ensureWritable(16+msg.getData().length  );
        out.writeInt(12 + msg.getData().length);
      //  out.writeInt(msg.getToken());
       // out.writeInt(msg.getUserId());
        out.writeInt(msg.getMessageId());
        out.writeBytes(msg.getData());


    }
}