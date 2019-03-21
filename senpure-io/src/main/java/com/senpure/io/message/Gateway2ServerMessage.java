package com.senpure.io.message;

import com.senpure.io.support.MessageIdReader;
import io.netty.buffer.ByteBuf;


public class Gateway2ServerMessage {

    private long token;
    private int messageId;

    private long userId;

    private ByteBuf buf;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }


    public ByteBuf getBuf() {
        return buf;
    }

    public void setBuf(ByteBuf buf) {
        this.buf = buf;
    }


    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "GS{" +
                "token=" + token +
                ", messageId=" +  MessageIdReader.read(messageId)  +
                ", userId=" + userId +
                ", buf=" + buf.writerIndex() +
                '}';
    }
}
