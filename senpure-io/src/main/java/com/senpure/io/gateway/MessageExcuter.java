package com.senpure.io.gateway;

import com.senpure.io.message.Client2GatewayMessage;

/**
 * MessageExcuter
 *
 * @author senpure
 * @time 2018-10-25 15:54:53
 */
public interface MessageExcuter {


    void execute(Client2GatewayMessage message);
}
