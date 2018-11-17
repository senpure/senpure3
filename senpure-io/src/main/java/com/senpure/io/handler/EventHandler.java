package com.senpure.io.handler;


import com.senpure.io.protocol.Event;

/**
 * EventHandler
 *
 * @author senpure
 * @time 2018-11-16 10:37:56
 */
public interface EventHandler<T extends Event> {

    T getEmptyEvent();

    int handlerId();

    void execute(T event);

}
