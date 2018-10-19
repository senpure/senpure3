package com.senpure.io.message;

import com.senpure.io.protocol.Message;

import java.util.Arrays;


public class Server2GatewayMessage {

    private Long[] userIds;

    private long token;
    private int messageId;
    //具体服务器上有值
    private Message message;
    //网关解析出来有值
    private byte[] data;





    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long[] getUserIds() {
        return userIds;
    }

    public void setUserIds(Long[] userIds) {
        this.userIds = userIds;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Server2GatewayMessage{" +
                "userIds=" + Arrays.toString(userIds) +
                ", token=" + token +
                ", messageId=" + messageId +
                ", data=" + data==null?"":data.length+""+
                '}';
    }
}
