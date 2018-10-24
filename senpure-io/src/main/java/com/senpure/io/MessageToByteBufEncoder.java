package com.senpure.io;


import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageToByteBufEncoder extends MessageToByteEncoder<Message> {
    protected Logger logger = LoggerFactory.getLogger(getClass());


    //包长int ,消息Id int, 二进制数据
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {

        int length = message.getSerializedSize();
       // logger.debug("message length {}", length);
        //head 4 +messageId 4+ content length

        out.ensureWritable(8 + length);
        out.writeInt(length + 4);
        out.writeInt(message.getMessageId());

        message.write(out);

       // logger.debug("out length {}", out.writerIndex());


    }
}
