package com.senpure.io.gateway;

import com.senpure.base.util.Assert;
import com.senpure.io.protocol.Constant;
import com.senpure.io.message.CSAskHandleMessage;
import com.senpure.io.message.Client2GatewayMessage;
import com.senpure.io.message.SCInnerErrorMessage;
import com.senpure.io.protocol.Bean;
import com.senpure.io.support.MessageIdReader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前设计direct 会执行一个具体的服务器绑定
 * 所以 direct 与 serverShare 不能同时为true
 */
public class HandleMessageManager {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private boolean direct;
    private boolean serverShare;
    private List<ServerManager> serverManagers = new ArrayList<>();
    private ServerManager serverManager;
    private GatewayMessageExecuter messageExecuter;
    private int csAskHandleMessageId = new CSAskHandleMessage().getMessageId();
    //   private AtomicInteger atomicIndex = new AtomicInteger(-1);
    private int handId;

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
                Assert.error("direct 与 serverShare 不能同时为true 不同的服务处理了相同的消息id ");
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
            ByteBuf buf = Unpooled.buffer(message.getData().length);
            buf.writeBytes(message.getData());
            String value;
            try {
                Bean.readTag(buf, buf.writerIndex());
                value = Bean.readString(buf);
            } catch (Exception e) {
                logger.error("读取询问值出错询问值只能是string 类型 messageId " + message.getMessageId(), e);
                // Assert.error("读取询问值出错 询问值只能是string 类型 messageId  " + message.getMessageId());

                SCInnerErrorMessage errorMessage = new SCInnerErrorMessage();
                errorMessage.setType(Constant.ERROR_SERVER_ERROR);
                errorMessage.setId(message.getMessageId());
                errorMessage.setMessage("询问值只能是String类型" + MessageIdReader.read(message.getMessageId()));
                messageExecuter.sendMessage2Client(errorMessage, message.getToken());
                return;
            }
            CSAskHandleMessage askHandleMessage = new CSAskHandleMessage();
            askHandleMessage.setFromMessageId(message.getMessageId());
            askHandleMessage.setToken(messageExecuter.idGenerator.nextId());
            askHandleMessage.setValue(value);
            Client2GatewayMessage temp = new Client2GatewayMessage();
            temp.setMessageId(csAskHandleMessageId);
            temp.setToken(message.getToken());
            temp.setUserId(message.getUserId());
            buf = Unpooled.buffer();
            buf.ensureWritable(askHandleMessage.getSerializedSize());
            askHandleMessage.write(buf);
            byte[] data = new byte[askHandleMessage.getSerializedSize()];
            buf.readBytes(data);
            temp.setData(data);
            WaitAskTask waitAskTask = new WaitAskTask(messageExecuter.getGateway().getAskMaxDelay());
            waitAskTask.setAskToken(askHandleMessage.getToken());
            waitAskTask.setFromMessageId(askHandleMessage.getFromMessageId());
            waitAskTask.setValue(value);

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
