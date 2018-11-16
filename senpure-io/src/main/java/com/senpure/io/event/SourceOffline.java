package com.senpure.io.event;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Source
 *
 * @author senpure
 * @time 2018-11-15 20:00:29
 */
public interface SourceOffline {
    String EVENT_OFFLINE_OUTPUT = "eventOfflineOutput";

    @Output(EVENT_OFFLINE_OUTPUT)
    MessageChannel output();
}
