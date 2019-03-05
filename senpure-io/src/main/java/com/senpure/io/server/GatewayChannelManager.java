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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class GatewayChannelManager {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static AtomicInteger atomicCount = new AtomicInteger(0);
    private List<Channel> channels = new ArrayList<>(16);

    private AtomicInteger atomicIndex = new AtomicInteger(-1);


    private boolean connecting = false;

    private ReadWriteLock connectLock = new ReentrantReadWriteLock();

    /**
     * GatewayChannelManager 的唯一标识
     */
    private int gatewayChannelKey;

    private String gatewayKey;



    public GatewayChannelManager(String gatewayKey) {
        this.gatewayKey = gatewayKey;
        gatewayChannelKey = atomicCount.incrementAndGet();
    }


    public void addChannel(Channel channel) {
        channels.add(channel);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }


    public void sendMessage(Server2GatewayMessage message) {


        nextChannel().writeAndFlush(message);
    }

    private Channel nextChannel() {
        if (channels.size() == 0) {
            logger.warn("{}没有可用得channel ", gatewayKey);
            return null;
        }
        return channels.get(nextIndex());
    }

    public int getGatewayChannelKey() {
        return gatewayChannelKey;
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


    public boolean isConnecting() {
        boolean temp;
        connectLock.readLock().lock();
        temp = connecting;
        connectLock.readLock().unlock();
        return temp;
    }

    public void setConnecting(boolean connecting) {
        connectLock.writeLock().lock();
        this.connecting = connecting;
        connectLock.writeLock().unlock();

    }

    public int getChannelSize() {
        return channels.size();
    }

    @Override
    public String toString() {
        return "GatewayChannelManager{" +
                "channels=" + channels +
                ", connecting=" + connecting +
                ", gatewayChannelKey=" + gatewayChannelKey +
                ", gatewayKey='" + gatewayKey + '\'' +
                '}';
    }

    public static void main(String[] args) {
        ConcurrentMap<Integer, Integer> ids = new ConcurrentHashMap<>();
        System.out.println(ids.putIfAbsent(1, 1));
        System.out.println(ids.putIfAbsent(1, 2));
        System.out.println(ids.putIfAbsent(1, 3));
    }
}
