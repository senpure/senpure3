package com.senpure.io.server;

import com.senpure.base.util.Assert;
import com.senpure.io.message.Gateway2ServerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解析网关发过来的消息
 */
public class RealityMessageDecoder extends ByteToMessageDecoder {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int rl = in.readableBytes();
        if (rl < 4) {
            this.logger.debug("数据过短 s{}", Integer.valueOf(rl));
        } else {
            in.markReaderIndex();
            int packageLength = in.readInt();
            if (packageLength == 0) {
                Assert.error("错误，数据包长度不能为0");
            }
            if (packageLength > in.readableBytes()) {
                if (packageLength > 2000000) {
                    ctx.close().sync();
                    return;
                }
                this.logger.info("数据不够一个数据包 packageLength ={} ,readableBytes={}", Integer.valueOf(packageLength), Integer.valueOf(in.readableBytes()));
                in.resetReaderIndex();
            } else {
                int messageId = in.readInt();
                long token = in.readLong();
                long userId = in.readLong();
                Gateway2ServerMessage message = new Gateway2ServerMessage();
                ByteBuf buf = in.readBytes(packageLength - 20);
                message.setMessageId(messageId);
                message.setToken(token);
                message.setBuf(buf);
                message.setUserId(userId);
                out.add(message);
            }

        }
    }
}
