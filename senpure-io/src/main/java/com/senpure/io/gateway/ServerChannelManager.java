package com.senpure.io.gateway;

import com.senpure.io.ChannelAttributeUtil;
import com.senpure.io.message.Client2GatewayMessage;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;


public class ServerChannelManager {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // private ConcurrentMap<Integer, Channel> serverChannels = new ConcurrentHashMap<>();
    protected List<Channel> channels = new ArrayList<>(16);

    private AtomicInteger atomicIndex = new AtomicInteger(-1);

    private Set<Integer> handleIds = new HashSet<>();

    private long start;
    private long end;


    private String serverKey;


    public void sendMessage(Client2GatewayMessage message) {
        Channel channel = nextChannel();
        if (channel == null) {
            logger.warn("{} 没有可用得channel", serverKey);
            return;
        }
        channel.writeAndFlush(message);

    }

    public boolean isActive() {
        return channels.size() > 0;
    }

    public boolean handleRange(long num) {
        return num >= start && num <= end;
    }

    public synchronized void addChannel(Channel channel) {
        channels.add(channel);
    }

    public synchronized boolean offline(Channel channel) {
        for (int i = 0; i < channels.size(); i++) {
            if (channel == channels.get(i)) {
                logger.info("{} {}与网关断开连接", ChannelAttributeUtil.getRemoteServerName(channel), channel);
                channels.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean handle(long value) {
        return value >= start && value <= end;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public boolean handleMessageId(int messageId) {
        return handleIds.contains(messageId);

    }

    public void markMessageId(int messageId) {
        handleIds.add(messageId);
    }

    public Channel nextChannel() {
        if (channels.size() == 0) {
            return null;
        }
        return channels.get(nextIndex());
    }

    private int nextIndex() {
        if (channels.size() == 1) {
            return 0;
        }
        int index = atomicIndex.incrementAndGet();
        return Math.abs(index % channels.size());
//        int index = atomicIndex.incrementAndGet();
//        if (index >= channels.size()) {
//            boolean reset = atomicIndex.compareAndSet(index, 0);
//            if (!reset) {
//                return nextIndex();
//            }
//            return 0;
//        }
        //  return index;
    }


    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }


    public static void main(String[] args) {
        ConcurrentMap<Integer, Integer> ids = new ConcurrentHashMap<>();


        System.out.println(ids.putIfAbsent(1, 1));
        System.out.println(ids.putIfAbsent(1, 2));
        System.out.println(ids.putIfAbsent(1, 3));
    }
}
