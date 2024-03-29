package com.senpure.io.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * SinkOffline
 *
 * @author senpure
 * @time 2018-11-16 10:08:27
 */
public interface SinkOffline {

    String OFFLINE_INPUT = "offline-input";
    @Input(OFFLINE_INPUT)
    SubscribableChannel input();
}
