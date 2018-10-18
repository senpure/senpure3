package com.senpure.io.server;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;


public class GatewayChannelManager {

    private static AtomicInteger atomicCount = new AtomicInteger(0);
    private List<Channel> channels = new ArrayList<>(16);

    private AtomicInteger atomicIndex = new AtomicInteger(-1);

    private int number = 0;

    public GatewayChannelManager() {
        number = atomicCount.incrementAndGet();
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
    }


    public Channel nextChannel() {

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


    public static void main(String[] args) {
        ConcurrentMap<Integer, Integer> ids = new ConcurrentHashMap<>();
        System.out.println(ids.putIfAbsent(1, 1));
        System.out.println(ids.putIfAbsent(1, 2));
        System.out.println(ids.putIfAbsent(1, 3));
    }
}