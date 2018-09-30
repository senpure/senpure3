package com.senpure.io.message;

import io.netty.buffer.ByteBuf;


public class Gateway2ServerMessage  {

    private int token;
    private int messageId;

    private int playerId;

    private ByteBuf buf;

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



    public ByteBuf getBuf() {
        return buf;
    }

    public void setBuf(ByteBuf buf) {
        this.buf = buf;
    }


    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Gateway2ServerMessage{" +
                "token=" + token +
                ", messageId=" + messageId +
                ", playerId=" + playerId +
                ", buf=" + buf.writerIndex()+
                '}';
    }
}
