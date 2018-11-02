package com.senpure.io.gateway;

import com.senpure.io.message.CSBreakUserGatewayMessage;
import com.senpure.io.message.Client2GatewayMessage;

/**
 * WaitRelationTask
 *
 * @author senpure
 * @time 2018-11-01 14:04:52
 */
public class WaitRelationTask  {

    private boolean relation = false;
    protected long startTime;
    private long relationTime;
    private long maxDelay = 5000;

    private Client2GatewayMessage message;

    private ServerChannelManager serverChannelManager;

    private ServerManager serverManager;
    private Long relationToken;


    public WaitRelationTask() {
        startTime = System.currentTimeMillis();
    }

    public boolean check() {

        return relation;
    }

    public boolean cancel() {
        if (relation) {
            return false;
        }
        return System.currentTimeMillis() - startTime > maxDelay;
    }


    public void sendMessage() {
        serverManager.bind(message.getUserId(), serverChannelManager);
        serverManager.sendMessage(message);
    }

    public void sendCancelMessage(GatewayMessageExecuter messageExecuter) {
        CSBreakUserGatewayMessage breakMessage = new CSBreakUserGatewayMessage();
        breakMessage.setRelationToken(relationToken);
        breakMessage.setToken(message.getToken());
        breakMessage.setUserId(message.getUserId());
        messageExecuter.sendMessage(serverChannelManager, breakMessage);

    }


    public Client2GatewayMessage getMessage() {
        return message;
    }

    public void setMessage(Client2GatewayMessage message) {
        this.message = message;
    }

    public ServerChannelManager getServerChannelManager() {
        return serverChannelManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public void setServerManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    public void setServerChannelManager(ServerChannelManager serverChannelManager) {
        this.serverChannelManager = serverChannelManager;
    }






    public Long getRelationToken() {
        return relationToken;
    }

    public void setRelationToken(Long relationToken) {
        this.relationToken = relationToken;
    }

    public boolean isRelation() {
        return relation;
    }

    public void setRelation(boolean relation) {
        this.relation = relation;
    }



    public long getRelationTime() {
        return relationTime;
    }

    public void setRelationTime(long relationTime) {
        this.relationTime = relationTime;
    }
}
