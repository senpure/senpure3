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
 * @time 2019-2-20 17:02:07
 */
public class SCRegServerHandleMessageMessage extends  Message {

    public static final int MESSAGE_ID = 1104;
    //服务器名
    private String serverName;
    //ip和第一个实例端口号
    private String ipAndFirstPort;
    //服务器名
    private String readableServerName;
    //可以处理的消息
    private List<HandleMessage> messages = new ArrayList(16);
    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf){
        getSerializedSize();
        //服务器名
        if (serverName != null){
            writeString(buf,8,serverName);
        }
        //ip和第一个实例端口号
        if (ipAndFirstPort != null){
            writeString(buf,16,ipAndFirstPort);
        }
        //服务器名
        if (readableServerName != null){
            writeString(buf,24,readableServerName);
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
                //服务器名
                case 8:// 1 << 3 | 0
                        serverName = readString(buf);
                    break;
                //ip和第一个实例端口号
                case 16:// 2 << 3 | 0
                        ipAndFirstPort = readString(buf);
                    break;
                //服务器名
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
        //服务器名
        if (serverName != null){
            size += computeStringSize(1,serverName);
        }
        //ip和第一个实例端口号
        if (ipAndFirstPort != null){
            size += computeStringSize(1,ipAndFirstPort);
        }
        //服务器名
        if (readableServerName != null){
            size += computeStringSize(1,readableServerName);
        }
        //可以处理的消息
        for(int i=0;i< messages.size();i++){
            size += computeBeanSize(1,messages.get(i));
        }
        serializedSize = size ;
        return size ;
    }

    /**
     * get 服务器名
     * @return
     */
    public  String getServerName() {
        return serverName;
    }

    /**
     * set 服务器名
     */
    public SCRegServerHandleMessageMessage setServerName(String serverName) {
        this.serverName=serverName;
        return this;
    }
    /**
     * get ip和第一个实例端口号
     * @return
     */
    public  String getIpAndFirstPort() {
        return ipAndFirstPort;
    }

    /**
     * set ip和第一个实例端口号
     */
    public SCRegServerHandleMessageMessage setIpAndFirstPort(String ipAndFirstPort) {
        this.ipAndFirstPort=ipAndFirstPort;
        return this;
    }
    /**
     * get 服务器名
     * @return
     */
    public  String getReadableServerName() {
        return readableServerName;
    }

    /**
     * set 服务器名
     */
    public SCRegServerHandleMessageMessage setReadableServerName(String readableServerName) {
        this.readableServerName=readableServerName;
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
                +",ipAndFirstPort=" + ipAndFirstPort
                +",readableServerName=" + readableServerName
                +",messages=" + messages
                + "}";
   }

    //18 + 3 = 21 个空格
    private String nextIndent ="                     ";
    //最长字段长度 18
    private int filedPad = 18;

    @Override
    public String toString(String indent) {
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCRegServerHandleMessageMessage").append("[1104]").append("{");
        //服务器名
        sb.append("\n");
        sb.append(indent).append(rightPad("serverName", filedPad)).append(" = ").append(serverName);
        //ip和第一个实例端口号
        sb.append("\n");
        sb.append(indent).append(rightPad("ipAndFirstPort", filedPad)).append(" = ").append(ipAndFirstPort);
        //服务器名
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