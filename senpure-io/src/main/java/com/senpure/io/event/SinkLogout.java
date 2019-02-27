package com.senpure.io.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Source
 *
 * @author senpure
 * @time 2018-11-15 20:00:29
 */
public interface SinkLogout {

    String LOGOUT_INPUT = "logout-input";

    @Input(LOGOUT_INPUT)
    SubscribableChannel input();
}
