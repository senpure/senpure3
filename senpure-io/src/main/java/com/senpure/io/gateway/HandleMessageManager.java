package com.senpure.io.gateway;

import com.senpure.base.util.Assert;
import com.senpure.io.message.CSAskHandleMessage;
import com.senpure.io.message.Client2GatewayMessage;
import com.senpure.io.protocol.Bean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 当前设计direct 会执行一个具体的服务器绑定
 * 所以 direct 与 serverShare 不能同时为true
 */
public class HandleMessageManager {


    private boolean direct;
    private boolean serverShare;
    private List<ServerManager> serverManagers = new ArrayList<>();
    private ServerManager serverManager;
    private GatewayMessageExecuter messageExecuter;
    private int csAskHandleMessageId = new CSAskHandleMessage().getMessageId();
    private AtomicInteger atomicIndex = new AtomicInteger(-1);
    private int handId = 0;

    public HandleMessageManager(int handId, boolean direct, boolean serverShare, GatewayMessageExecuter messageExecuter) {
        this.direct = direct;
        this.serverShare = serverShare;
        this.messageExecuter = messageExecuter;
        this.handId = handId;
        if (direct && serverShare) {
            Assert.error("direct 与 serverShare 不能同时为true ");
        }

    }

    public synchronized void addServerManager(int handId, ServerManager serverManager) {
        if (this.handId != handId) {

            Assert.error("handId 不匹配");
        }
        boolean add = true;
        for (ServerManager manager : serverManagers) {
            if (manager.getServerName().equalsIgnoreCase(serverManager.getServerName())) {
                add = false;
                break;
            }
        }
        if (add) {
            if (serverManagers.size() >= 1 && direct) {
                Assert.error("direct 与 serverShare 不能同时为true ");
            }
            serverManagers.add(serverManager);
        }
        if (direct) {
            this.serverManager = serverManager;
        }
    }

    public void execute(Client2GatewayMessage message) {
        if (direct) {
            serverManager.sendMessage(message);
        } else {
            ByteBuf buf = Unpooled.buffer();
            buf.writeBytes(message.getData());
            String value = Bean.readString(buf);
            CSAskHandleMessage askHandleMessage = new CSAskHandleMessage();
            askHandleMessage.setFromMessageId(message.getMessageId());
            askHandleMessage.setToken(messageExecuter.idGenerator.nextId());
            askHandleMessage.setValue(value);
            Client2GatewayMessage temp = new Client2GatewayMessage();
            temp.setMessageId(csAskHandleMessageId);
            temp.setToken(message.getToken());
            temp.setUserId(message.getUserId());
            buf = Unpooled.buffer();
            askHandleMessage.write(buf);
            temp.setData(buf.array());

            WaitAskTask waitAskTask = new WaitAskTask();
            waitAskTask.setAskToken(askHandleMessage.getToken());
            int askTimes = 0;
            for (ServerManager serverManager : serverManagers) {
                askTimes += serverManager.getUseChannelManagers().size();
            }
            waitAskTask.setAskTimes(askTimes);
            waitAskTask.setMessage(message);

            messageExecuter.waitAskMap.put(waitAskTask.getAskToken(), waitAskTask);
            for (ServerManager serverManager : serverManagers) {
                for (ServerChannelManager channelManager : serverManager.getUseChannelManagers()) {
                    Channel channel = channelManager.nextChannel();
                    if (channel != null) {
                        channel.writeAndFlush(temp);
                    }
                }
            }
        }
    }

    private int nextIndex() {
        if (serverManagers.size() == 0) {
            return 0;
        }
        int index = atomicIndex.incrementAndGet();
        return Math.abs(index % serverManagers.size());
    }


    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public boolean isServerShare() {
        return serverShare;
    }

    public void setServerShare(boolean serverShare) {
        this.serverShare = serverShare;
    }

}
