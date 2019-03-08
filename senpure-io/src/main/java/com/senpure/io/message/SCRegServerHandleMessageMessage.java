package com.senpure.io.message;

import com.senpure.io.bean.HandleMessage;
import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.ArrayList;

/**
 * 服务器注册消息处理器到网关
 * 
 * @author senpure
 * @time 2019-3-8 16:16:07
 */
public class SCRegServerHandleMessageMessage extends  Message {

    public static final int MESSAGE_ID = 1104;
    //服务名
    private String serverName;
    //服务实例唯一标识
    private String serverKey;
    //服务名
    private String readableServerName;
    //可以处理的消息
    private List<HandleMessage> messages = new ArrayList(16);
    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf){
        getSerializedSize();
        //服务名
        if (serverName != null){
            writeString(buf,8,serverName);
        }
        //服务实例唯一标识
        if (serverKey != null){
            writeString(buf,16,serverKey);
        }
        //服务名
        if (readableServerName != null){
            writeString(buf,24, readableServerName);
        }
        //可以处理的消息
        for (int i= 0;i< messages.size();i++){
            writeBean(buf,35,messages.get(i));
        }
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
                //服务名
                case 8:// 1 << 3 | 0
                        serverName = readString(buf);
                    break;
                //服务实例唯一标识
                case 16:// 2 << 3 | 0
                        serverKey = readString(buf);
                    break;
                //服务名
                case 24:// 3 << 3 | 0
                        readableServerName = readString(buf);
                    break;
                //可以处理的消息
                case 35:// 4 << 3 | 3
                        HandleMessage tempMessagesBean = new HandleMessage();
                        readBean(buf,tempMessagesBean);
                        messages.add(tempMessagesBean);
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
        //服务名
        if (serverName != null){
            size += computeStringSize(1,serverName);
        }
        //服务实例唯一标识
        if (serverKey != null){
            size += computeStringSize(1,serverKey);
        }
        //服务名
        if (readableServerName != null){
            size += computeStringSize(1, readableServerName);
        }
        //可以处理的消息
        for(int i=0;i< messages.size();i++){
            size += computeBeanSize(1,messages.get(i));
        }
        serializedSize = size ;
        return size ;
    }

    /**
     * get 服务名
     * @return
     */
    public  String getServerName() {
        return serverName;
    }

    /**
     * set 服务名
     */
    public SCRegServerHandleMessageMessage setServerName(String serverName) {
        this.serverName=serverName;
        return this;
    }
    /**
     * get 服务实例唯一标识
     * @return
     */
    public  String getServerKey() {
        return serverKey;
    }

    /**
     * set 服务实例唯一标识
     */
    public SCRegServerHandleMessageMessage setServerKey(String serverKey) {
        this.serverKey=serverKey;
        return this;
    }
    /**
     * get 服务名
     * @return
     */
    public  String getReadableServerName() {
        return readableServerName;
    }

    /**
     * set 服务名
     */
    public SCRegServerHandleMessageMessage setReadableServerName(String readableServerName) {
        this.readableServerName = readableServerName;
        return this;
    }
     /**
      * get 可以处理的消息
      * @return
      */
    public List<HandleMessage> getMessages(){
        return messages;
    }
     /**
      * set 可以处理的消息
      */
    public SCRegServerHandleMessageMessage setMessages (List<HandleMessage> messages){
        if(messages == null){
        this.messages = new ArrayList(16);
            return this;
        }
        this.messages=messages;
        return this;
    }


    @Override
    public int getMessageId() {
        return 1104;
    }

    @Override
    public String toString() {
        return "SCRegServerHandleMessageMessage[1104]{"
                +"serverName=" + serverName
                +",serverKey=" + serverKey
                +",readableServerName=" + readableServerName
                +",messages=" + messages
                + "}";
   }

    //12 + 3 = 15 个空格
    private String nextIndent ="               ";
    //最长字段长度 12
    private int filedPad = 12;

    @Override
    public String toString(String indent) {
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCRegServerHandleMessageMessage").append("[1104]").append("{");
        //服务名
        sb.append("\n");
        sb.append(indent).append(rightPad("serverName", filedPad)).append(" = ").append(serverName);
        //服务实例唯一标识
        sb.append("\n");
        sb.append(indent).append(rightPad("serverKey", filedPad)).append(" = ").append(serverKey);
        //服务名
        sb.append("\n");
        sb.append(indent).append(rightPad("readableServerName", filedPad)).append(" = ").append(readableServerName);
        //可以处理的消息
        sb.append("\n");
        sb.append(indent).append(rightPad("messages", filedPad)).append(" = ");
        int messagesSize = messages.size();
        if (messagesSize > 0) {
            sb.append("[");
            for (int i = 0; i<messagesSize;i++) {
                sb.append("\n");
                sb.append(nextIndent);
                sb.append(indent).append(messages.get(i).toString(indent + nextIndent));
            }
            sb.append("\n");
        sb.append(nextIndent);
            sb.append(indent).append("]");
        }else {
            sb.append("null");
        }

        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}