package com.senpure.io.handler;


import com.senpure.io.protocol.Message;
import io.netty.channel.Channel;


public interface RealityMessageHandler<T extends Message> {


    T getEmptyMessage();

    void execute(Channel channel, long token, long playerId, T message);

    int handlerId();

    /**
     * 是否直接转发，false 网关会进行一次询问
     * @return
     */
    boolean  direct();


    /**
     * true 表示不同的服务器
     */
    boolean serverShare();


    /**
     * 内部请求不要注册到网关
     * @return
     */
    boolean regToGateway();

}
