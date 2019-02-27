package com.senpure.io.server;

import com.senpure.io.message.Server2GatewayMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RealityMessageEncoder extends MessageToByteEncoder<Server2GatewayMessage> {
    protected Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    protected void encode(ChannelHandlerContext ctx, Server2GatewayMessage message, ByteBuf out) throws Exception {
        ByteBuf buf = Unpooled.buffer(message.getMessage().getSerializedSize());
        message.getMessage().write(buf);
        int length = buf.writerIndex();
        //head 4 +messageId 4 token 8+ playerLen 2+userLen*8+ content length
        int userLen = message.getUserIds().length;
        int packageLen = 18 + (userLen << 3) + length;
        out.ensureWritable(packageLen);
        out.writeInt(packageLen - 4);
        out.writeInt(message.getMessageId());
        out.writeLong(message.getToken());
        out.writeShort(userLen);
        for (long i : message.getUserIds()) {
            out.writeLong(i);
        }
        out.writeBytes(buf);
    }


}
