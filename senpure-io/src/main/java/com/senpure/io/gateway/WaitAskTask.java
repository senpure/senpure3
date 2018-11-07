package com.senpure.io.gateway;

import com.senpure.io.message.Client2GatewayMessage;

/**
 * WaitAskTask
 *
 * @author senpure
 * @time 2018-11-01 18:03:18
 */
public class WaitAskTask {


    private long askToken;

    private String value;

    private long startTime;
    private long answerTime;
    private int askTimes;
    private int answerTimes;
    private long maxDelay = 5000;

    private ServerChannelManager serverChannelManager;

    private ServerManager serverManager;
    private Client2GatewayMessage message;

    public WaitAskTask() {
        startTime = System.currentTimeMillis();
    }

    public synchronized void answer(ServerManager serverManager, ServerChannelManager serverChannelManager, boolean canHandle) {
        answerTimes++;
        if (canHandle) {
            if (this.serverChannelManager != null) {
                return;
            }
            this.serverManager = serverManager;
            this.serverChannelManager = serverChannelManager;

            return;
        }
    }

    public void sendMessage() {
        serverManager.bindAndWaitSendMessage(serverChannelManager, message);

    }

    public boolean cancel() {
        if (serverChannelManager != null) {
            return false;
        }
        return System.currentTimeMillis() - startTime > maxDelay;
    }

    public ServerChannelManager getServerChannelManager() {
        return serverChannelManager;
    }

    public void setServerChannelManager(ServerChannelManager serverChannelManager) {
        this.serverChannelManager = serverChannelManager;
    }

    public long getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(long answerTime) {
        this.answerTime = answerTime;
    }

    public long getAskToken() {
        return askToken;
    }

    public void setAskToken(long askToken) {
        this.askToken = askToken;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


    public Client2GatewayMessage getMessage() {
        return message;
    }

    public void setMessage(Client2GatewayMessage message) {
        this.message = message;
    }

    public int getAnswerTimes() {
        return answerTimes;
    }

    public void setAnswerTimes(int answerTimes) {
        this.answerTimes = answerTimes;
    }

    public int getAskTimes() {
        return askTimes;
    }

    public void setAskTimes(int askTimes) {
        this.askTimes = askTimes;
    }
}
