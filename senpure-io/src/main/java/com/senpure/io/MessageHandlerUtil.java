package com.senpure.io;


import com.senpure.base.util.Assert;
import com.senpure.io.handler.MessageHandler;
import com.senpure.io.protocol.Message;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;


public class MessageHandlerUtil {
    private static Logger logger = LoggerFactory.getLogger(MessageHandlerUtil.class);
    private static Map<Integer, MessageHandler> handlerMap = new HashMap<>();

    private static ScheduledExecutorService service;


    public static void setService(ScheduledExecutorService service) {
        MessageHandlerUtil.service = service;
    }

    public static void execute(Runnable runnable) {
        service.execute(runnable);
    }


    public static boolean isShutdown() {
       return service.isShutdown();
    }
    public static void execute(Channel channel, Message message) {

        service.execute(() -> {
            try {
                handlerMap.get(message.getMessageId()).execute(channel, message);
            } catch (Exception e) {
                logger.error("执行handler[" + handlerMap.get(message.getMessageId()).getClass().getName() + "]逻辑出错 ", e);

            }
        });
    }

    public static MessageHandler getHandler(int messageId) {
        return handlerMap.get(messageId);
    }

    public static void regMessageHandler(MessageHandler handler) {
        Assert.isNull(handlerMap.get(handler.handlerId()), handler.handlerId() + " -> " + handler.getEmptyMessage().getClass().getName() + "  处理程序已经存在");
        handlerMap.put(handler.handlerId(), handler);
    }

    public static Message getEmptyMessage(int messageId) {
        MessageHandler handler = handlerMap.get(messageId);
        if (handler == null) {
            return null;
        }
        return handler.getEmptyMessage();
    }
}
