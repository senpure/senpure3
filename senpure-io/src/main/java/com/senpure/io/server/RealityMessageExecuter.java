package com.senpure.io.server;

import com.senpure.io.RealityMessageHandlerUtil;
import com.senpure.io.handler.RealityMessageHandler;
import com.senpure.io.message.Gateway2ServerMessage;
import com.senpure.io.protocol.Message;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RealityMessageExecuter {
    private ExecutorService service;

    public RealityMessageExecuter() {

        service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }

    public RealityMessageExecuter(ExecutorService service) {
        this.service = service;

    }

    private Logger logger = LoggerFactory.getLogger(RealityMessageExecuter.class);

    public void execute(Runnable runnable) {
        service.execute(runnable);
    }

    public void execute(Channel channel, Gateway2ServerMessage gsMessage) {
        service.execute(() -> {
            long userId = gsMessage.getUserId();
            RealityMessageHandler handler = RealityMessageHandlerUtil.getHandler(gsMessage.getMessageId());
            if (handler == null) {
                logger.warn("没有找到消息出来程序{} userId:{}", gsMessage.getMessageId(), userId);
                return;
            }
            Message message = handler.getEmptyMessage();
            message.read(gsMessage.getBuf(), gsMessage.getBuf().writerIndex());
            handler.execute(channel, gsMessage.getToken(), userId, message);

        });
    }


}
