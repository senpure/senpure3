package com.senpure.io.message;

import java.util.Arrays;


public class Client2GatewayMessage {
    private int messageId;

    private long userId;
    private long token;

    private byte[] data;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setToken(int token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Client2GatewayMessage{" +
                "messageId=" + messageId +
                ", userId=" + userId +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
