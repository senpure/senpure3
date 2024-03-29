package com.senpure.io.server;


import com.senpure.io.IOMessageProperties;
import com.senpure.io.message.Server2GatewayMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class RealityMessageLoggingHandler extends LoggingHandler {
    private IOMessageProperties config;

    public RealityMessageLoggingHandler(LogLevel level, IOMessageProperties config) {
        super(level);
        this.config = config;
    }


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        if (this.logger.isEnabled(this.internalLevel)) {
            if (msg instanceof Server2GatewayMessage) {
                if (config.isOutFormat()) {
                    this.logger.log(this.internalLevel, "{} {} {}{}",
                            ctx.channel(),
                            "WRITE", "\n", ((Server2GatewayMessage) msg).toString(null));
                    //this.logger.log(this.internalLevel, this.format(ctx, ChannelAttributeUtil.getChannelPlayerStr(ctx.channel())+" WRITE", "\n"+((Message) msg).toString(null)));
                } else {
                    this.logger.log(this.internalLevel, "{} {} {}",
                            ctx.channel(),
                            "WRITE: ", msg);
                    //  this.logger.log(this.internalLevel, this.format(ctx, ChannelAttributeUtil.getChannelPlayerStr(ctx.channel())+" WRITE", msg));

                }

            } else {
                this.logger.log(this.internalLevel, "{} {} {}",
                        ctx.channel(),
                        "WRITE: ", msg);
            }
        }

        ctx.write(msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (this.logger.isEnabled(this.internalLevel)) {
            if (msg instanceof Server2GatewayMessage) {
                if (config.isInFormat()) {
                    this.logger.log(this.internalLevel, "{} {} {}{}",
                            ctx.channel(),
                            "RECEIVED", "\n", ((Server2GatewayMessage) msg).toString(null));
                    // this.logger.log(this.internalLevel, this.format(ctx, ChannelAttributeUtil.getChannelPlayerStr(ctx.channel()) + " RECEIVED", "\n" + ((Message) msg).toString(null)));

                } else {
                    this.logger.log(this.internalLevel, "{} {} {}{}",
                            ctx.channel(),
                            "RECEIVED: ", msg);
                }
            } else {
                this.logger.log(this.internalLevel, "{} {} {}{}",
                        ctx.channel(),
                        "RECEIVED: ", msg);
            }
        }
        ctx.fireChannelRead(msg);

    }
}
