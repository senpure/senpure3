package com.senpure.io.message;

import java.util.Arrays;


public class Client2GatewayMessage {
    private int messageId;

    private int playerId;
    private int token;

    private byte[] data;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Client2GatewayMessage{" +
                "messageId=" + messageId +
                ", playerId=" + playerId +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
