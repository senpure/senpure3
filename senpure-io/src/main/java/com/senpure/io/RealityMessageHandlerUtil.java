package com.senpure.io;


import com.senpure.base.util.Assert;
import com.senpure.io.handler.RealityMessageHandler;
import com.senpure.io.protocol.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RealityMessageHandlerUtil {
    private static Map<Integer, RealityMessageHandler> handlerMap = new HashMap<>();

    public static RealityMessageHandler getHandler(int messageId) {
        return handlerMap.get(messageId);
    }

    private static List<Integer> regMessageIds = new ArrayList<>(128);

    public static void regMessageHandler(RealityMessageHandler handler) {
        Assert.isNull(handlerMap.get(handler.handlerId()), handler.handlerId() + " -> " + handler.getEmptyMessage().getClass().getName() + "  处理程序已经存在");

        handlerMap.put(handler.handlerId(), handler);
        if (handler.regToGateway()) {
            if (handler.handlerId() < 10000) {
                Assert.error("10000 以下为保留id不允许使用 " + handler.handlerId() + " " + handler.getEmptyMessage().getClass().getName());
            }
            regMessageIds.add(handler.handlerId());
        }
    }

    public static Message getEmptyMessage(int messageId) {
        RealityMessageHandler handler = handlerMap.get(messageId);
        if (handler == null) {
            return null;
        }
        return handler.getEmptyMessage();
    }

    public static List<Integer> getHandlerMessageIds() {
        return new ArrayList<>(regMessageIds);
    }
}
