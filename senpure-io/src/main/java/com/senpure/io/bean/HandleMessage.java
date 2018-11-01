package com.senpure.io.bean;

import com.senpure.io.protocol.Bean;
import io.netty.buffer.ByteBuf;

/**
 * <br><b>index start:1 end:7</b>
 * 
 * @author senpure
 * @time 2018-11-1 15:06:13
 */
public class HandleMessage extends  Bean {
    //可以处理的消息ID
    private int handleMessageId;
    //消息类名
    private String messageClasses;
    //是否共享messageId 不同的服务都可以处理
    private boolean serverShare;
    //消息类型 0 可以直接转发过来 1 网关读取范围  2 网关询问
    private int messageType;
    //数字类型 0int 1 long
    private int valueType;
    //范围开始
    private long numStart;
    //范围结束
    private long numEnd;
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
        //消息类型 0 可以直接转发过来 1 网关读取范围  2 网关询问
        writeVar32(buf,32,messageType);
        //数字类型 0int 1 long
        writeVar32(buf,40,valueType);
        //范围开始
        writeVar64(buf,48,numStart);
        //范围结束
        writeVar64(buf,56,numEnd);
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
                //消息类型 0 可以直接转发过来 1 网关读取范围  2 网关询问
                case 32:// 4 << 3 | 0
                        messageType = readVar32(buf);
                    break;
                //数字类型 0int 1 long
                case 40:// 5 << 3 | 0
                        valueType = readVar32(buf);
                    break;
                //范围开始
                case 48:// 6 << 3 | 0
                        numStart = readVar64(buf);
                    break;
                //范围结束
                case 56:// 7 << 3 | 0
                        numEnd = readVar64(buf);
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
        //消息类型 0 可以直接转发过来 1 网关读取范围  2 网关询问
        size += computeVar32Size(1,messageType);
        //数字类型 0int 1 long
        size += computeVar32Size(1,valueType);
        //范围开始
        size += computeVar64Size(1,numStart);
        //范围结束
        size += computeVar64Size(1,numEnd);
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
     * get 消息类型 0 可以直接转发过来 1 网关读取范围  2 网关询问
     * @return
     */
    public  int getMessageType() {
        return messageType;
    }

    /**
     * set 消息类型 0 可以直接转发过来 1 网关读取范围  2 网关询问
     */
    public HandleMessage setMessageType(int messageType) {
        this.messageType=messageType;
        return this;
    }
    /**
     * get 数字类型 0int 1 long
     * @return
     */
    public  int getValueType() {
        return valueType;
    }

    /**
     * set 数字类型 0int 1 long
     */
    public HandleMessage setValueType(int valueType) {
        this.valueType=valueType;
        return this;
    }
    /**
     * get 范围开始
     * @return
     */
    public  long getNumStart() {
        return numStart;
    }

    /**
     * set 范围开始
     */
    public HandleMessage setNumStart(long numStart) {
        this.numStart=numStart;
        return this;
    }
    /**
     * get 范围结束
     * @return
     */
    public  long getNumEnd() {
        return numEnd;
    }

    /**
     * set 范围结束
     */
    public HandleMessage setNumEnd(long numEnd) {
        this.numEnd=numEnd;
        return this;
    }

    /**
     * set 可以处理的消息ID
     */
    public HandleMessage set1HandleMessageId(int handleMessageId) {
        this.handleMessageId=handleMessageId;
        return this;
    }

    /**
     * set 消息类名
     */
    public HandleMessage set2MessageClasses(String messageClasses) {
        this.messageClasses=messageClasses;
        return this;
    }

    /**
     * set 是否共享messageId 不同的服务都可以处理
     */
    public HandleMessage set3ServerShare(boolean serverShare) {
        this.serverShare=serverShare;
        return this;
    }

    /**
     * set 消息类型 0 可以直接转发过来 1 网关读取范围  2 网关询问
     */
    public HandleMessage set4MessageType(int messageType) {
        this.messageType=messageType;
        return this;
    }

    /**
     * set 数字类型 0int 1 long
     */
    public HandleMessage set5ValueType(int valueType) {
        this.valueType=valueType;
        return this;
    }

    /**
     * set 范围开始
     */
    public HandleMessage set6NumStart(long numStart) {
        this.numStart=numStart;
        return this;
    }

    /**
     * set 范围结束
     */
    public HandleMessage set7NumEnd(long numEnd) {
        this.numEnd=numEnd;
        return this;
    }

    @Override
    public String toString() {
        return "HandleMessage{"
                +"handleMessageId=" + handleMessageId
                +",messageClasses=" + messageClasses
                +",serverShare=" + serverShare
                +",messageType=" + messageType
                +",valueType=" + valueType
                +",numStart=" + numStart
                +",numEnd=" + numEnd
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
        //消息类型 0 可以直接转发过来 1 网关读取范围  2 网关询问
        sb.append("\n");
        sb.append(indent).append(rightPad("messageType", filedPad)).append(" = ").append(messageType);
        //数字类型 0int 1 long
        sb.append("\n");
        sb.append(indent).append(rightPad("valueType", filedPad)).append(" = ").append(valueType);
        //范围开始
        sb.append("\n");
        sb.append(indent).append(rightPad("numStart", filedPad)).append(" = ").append(numStart);
        //范围结束
        sb.append("\n");
        sb.append(indent).append(rightPad("numEnd", filedPad)).append(" = ").append(numEnd);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}