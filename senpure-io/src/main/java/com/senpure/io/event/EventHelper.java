package com.senpure.io.event;

import com.senpure.base.util.Assert;
import com.senpure.io.handler.EventHandler;
import com.senpure.io.protocol.Bean;
import com.senpure.io.protocol.Event;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * EventHelper
 *
 * @author senpure
 * @time 2018-11-15 19:33:53
 */
public class EventHelper {


    private static Logger logger = LoggerFactory.getLogger(EventHelper.class);
    private static Map<Integer, EventHandler> eventHandlerMap = new HashMap<>();
    private static ScheduledExecutorService service;


    public static void setService(ScheduledExecutorService service) {
        EventHelper.service = service;
    }

    public static void regEventHandler(EventHandler eventHandler) {
        Assert.isNull(eventHandlerMap.get(eventHandler.handlerId()), eventHandler.handlerId() + " -> " + eventHandler.getEmptyEvent().getClass().getName() + "  处理程序已经存在");

        eventHandlerMap.put(eventHandler.handlerId(), eventHandler);
    }

    private static Event getEmptyEvent(int eventId) {
        EventHandler eventHandler = eventHandlerMap.get(eventId);
        if (eventHandler != null) {
            return eventHandler.getEmptyEvent();
        }
        return null;
    }

    public static EventHandler getEventHandler(int eventId) {
        EventHandler eventHandler = eventHandlerMap.get(eventId);

        return eventHandler;
    }

    public static byte[] write(Event event) {
        ByteBuf byteBuf = Unpooled.buffer(Bean.computeVar32Size(event.getEventId()) + event.getSerializedSize());
        Bean.writeVar32(byteBuf, event.getEventId());
        event.write(byteBuf);
        return byteBuf.array();
    }

    public static Event read(byte[] bytes) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        int eventId = Bean.readVar32(byteBuf);
        Event event = getEmptyEvent(eventId);
        if (event == null) {
            logger.warn("没有注册事件{}", eventId);
            return null;
        }
        event.read(byteBuf, byteBuf.writerIndex());
        return event;
    }

    public static Event readAndExecute(byte[] bytes) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        int eventId = Bean.readVar32(byteBuf);
        EventHandler eventHandler = getEventHandler(eventId);
        if (eventHandler == null) {
            return null;
        }
        Event event = eventHandler.getEmptyEvent();
        event.read(byteBuf, byteBuf.writerIndex());
        service.execute(() -> eventHandler.execute(event));
        return event;
    }

    public static byte[] getBytes(Event event) {
        ByteBuf byteBuf = Unpooled.buffer(event.getSerializedSize());
        event.write(byteBuf);
        return byteBuf.array();
    }


}
