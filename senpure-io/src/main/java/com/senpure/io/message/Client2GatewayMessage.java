package com.senpure.io.message;

import com.senpure.io.support.MessageIdReader;


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


    @Override
    public String toString() {
        return "CG{" +
                "messageId=" + MessageIdReader.read(messageId) +
                ", userId=" + userId +
                ", dataLen=" + data.length +
                '}';
    }
}
