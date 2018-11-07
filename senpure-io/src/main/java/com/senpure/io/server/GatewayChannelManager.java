package com.senpure.io.server;

import com.senpure.io.message.Server2GatewayMessage;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;


public class GatewayChannelManager {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static AtomicInteger atomicCount = new AtomicInteger(0);
    private List<Channel> channels = new ArrayList<>(16);

    private AtomicInteger atomicIndex = new AtomicInteger(-1);

    private int number = 0;

    private String gatewayKey;
    public GatewayChannelManager(String gatewayKey) {
        this.gatewayKey = gatewayKey;
        number = atomicCount.incrementAndGet();
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
    }


    public void sendMessage(Server2GatewayMessage message) {


        nextChannel().writeAndFlush(message);
    }

    private  Channel nextChannel() {
        if (channels.size() == 0) {
            logger.warn("{}没有可用得channel ",gatewayKey);
            return null;
        }
        return channels.get(nextIndex());
    }

    public int getNumber() {
        return number;
    }

    private int nextIndex() {
        if (channels.size() == 1) {
            return 0;
        }
        int index = atomicIndex.incrementAndGet();
        if (index >= channels.size()) {
            boolean reset = atomicIndex.compareAndSet(index, 0);
            if (!reset) {
                return nextIndex();
            }
            return 0;
        }
        return index;
    }

    public String getGatewayKey() {
        return gatewayKey;
    }



    public static void main(String[] args) {
        ConcurrentMap<Integer, Integer> ids = new ConcurrentHashMap<>();
        System.out.println(ids.putIfAbsent(1, 1));
        System.out.println(ids.putIfAbsent(1, 2));
        System.out.println(ids.putIfAbsent(1, 3));
    }
}
