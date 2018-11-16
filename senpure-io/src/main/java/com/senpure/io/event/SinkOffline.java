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

    String EVENT_OFFLINE_INPUT = "eventOfflineInput";
    @Input(EVENT_OFFLINE_INPUT)
    SubscribableChannel input();
}
