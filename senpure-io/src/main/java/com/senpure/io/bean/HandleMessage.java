package com.senpure.io.bean;

import com.senpure.io.protocol.Bean;
import io.netty.buffer.ByteBuf;

/**
 * @author senpure
 * @time 2019-3-22 10:34:46
 */
public class HandleMessage extends  Bean {
    //可以处理的消息ID
    private int handleMessageId;
    //消息类名
    private String messageClasses;
    //是否共享messageId 不同的服务都可以处理
    private boolean serverShare;
    //true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
    private boolean direct;
    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf){
        getSerializedSize();
        //可以处理的消息ID
        writeVar32(buf,8,handleMessageId);
        //消息类名
        if (messageClasses != null){
            writeString(buf,16,messageClasses);
        }
        //是否共享messageId 不同的服务都可以处理
        writeBoolean(buf,24,serverShare);
        //true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
        writeBoolean(buf,32,direct);
    }

    /**
     * 读取字节缓存
     */
    @Override
    public void read(ByteBuf buf,int endIndex){
        while(true){
            int tag = readTag(buf, endIndex);
            switch (tag) {
                case 0://end
                return;
                //可以处理的消息ID
                case 8:// 1 << 3 | 0
                        handleMessageId = readVar32(buf);
                    break;
                //消息类名
                case 16:// 2 << 3 | 0
                        messageClasses = readString(buf);
                    break;
                //是否共享messageId 不同的服务都可以处理
                case 24:// 3 << 3 | 0
                        serverShare = readBoolean(buf);
                    break;
                //true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
                case 32:// 4 << 3 | 0
                        direct = readBoolean(buf);
                    break;
                default://skip
                    skip(buf, tag);
                    break;
            }
        }
    }

    private int serializedSize = -1;

    @Override
    public int getSerializedSize(){
        int size = serializedSize ;
        if (size != -1 ){
            return size;
        }
        size = 0 ;
        //可以处理的消息ID
        size += computeVar32Size(1,handleMessageId);
        //消息类名
        if (messageClasses != null){
            size += computeStringSize(1,messageClasses);
        }
        //是否共享messageId 不同的服务都可以处理
        size += computeBooleanSize(1,serverShare);
        //true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
        size += computeBooleanSize(1,direct);
        serializedSize = size ;
        return size ;
    }

    /**
     * get 可以处理的消息ID
     * @return
     */
    public  int getHandleMessageId() {
        return handleMessageId;
    }

    /**
     * set 可以处理的消息ID
     */
    public HandleMessage setHandleMessageId(int handleMessageId) {
        this.handleMessageId=handleMessageId;
        return this;
    }
    /**
     * get 消息类名
     * @return
     */
    public  String getMessageClasses() {
        return messageClasses;
    }

    /**
     * set 消息类名
     */
    public HandleMessage setMessageClasses(String messageClasses) {
        this.messageClasses=messageClasses;
        return this;
    }
    /**
     *  is 是否共享messageId 不同的服务都可以处理
     * @return
     */
    public  boolean  isServerShare() {
        return serverShare;
    }

    /**
     * set 是否共享messageId 不同的服务都可以处理
     */
    public HandleMessage setServerShare(boolean serverShare) {
        this.serverShare=serverShare;
        return this;
    }
    /**
     *  is true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
     * @return
     */
    public  boolean  isDirect() {
        return direct;
    }

    /**
     * set true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
     */
    public HandleMessage setDirect(boolean direct) {
        this.direct=direct;
        return this;
    }

    @Override
    public String toString() {
        return "HandleMessage{"
                +"handleMessageId=" + handleMessageId
                +",messageClasses=" + messageClasses
                +",serverShare=" + serverShare
                +",direct=" + direct
                + "}";
   }

    //最长字段长度 15
    private int filedPad = 15;

    @Override
    public String toString(String indent) {
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("HandleMessage").append("{");
        //可以处理的消息ID
        sb.append("\n");
        sb.append(indent).append(rightPad("handleMessageId", filedPad)).append(" = ").append(handleMessageId);
        //消息类名
        sb.append("\n");
        sb.append(indent).append(rightPad("messageClasses", filedPad)).append(" = ").append(messageClasses);
        //是否共享messageId 不同的服务都可以处理
        sb.append("\n");
        sb.append(indent).append(rightPad("serverShare", filedPad)).append(" = ").append(serverShare);
        //true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
        sb.append("\n");
        sb.append(indent).append(rightPad("direct", filedPad)).append(" = ").append(direct);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}