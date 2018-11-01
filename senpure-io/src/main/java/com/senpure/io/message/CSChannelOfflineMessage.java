package com.senpure.io.message;

import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;

/**
 * 客户端掉线
 * 
 * @author senpure
 * @time 2018-11-1 15:06:13
 */
public class CSChannelOfflineMessage extends  Message {
    //channel token
    private long token;
    //用户Id
    private long userId;
    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf){
        getSerializedSize();
        //channel token
        writeVar64(buf,8,token);
        //用户Id
        writeVar64(buf,16,userId);
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
                //channel token
                case 8:// 1 << 3 | 0
                        token = readVar64(buf);
                    break;
                //用户Id
                case 16:// 2 << 3 | 0
                        userId = readVar64(buf);
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
        //channel token
        size += computeVar64Size(1,token);
        //用户Id
        size += computeVar64Size(1,userId);
        serializedSize = size ;
        return size ;
    }

    /**
     * get channel token
     * @return
     */
    public  long getToken() {
        return token;
    }

    /**
     * set channel token
     */
    public CSChannelOfflineMessage setToken(long token) {
        this.token=token;
        return this;
    }
    /**
     * get 用户Id
     * @return
     */
    public  long getUserId() {
        return userId;
    }

    /**
     * set 用户Id
     */
    public CSChannelOfflineMessage setUserId(long userId) {
        this.userId=userId;
        return this;
    }

    @Override
    public int getMessageId() {
        return 1103;
    }

    @Override
    public String toString() {
        return "CSChannelOfflineMessage[1103]{"
                +"token=" + token
                +",userId=" + userId
                + "}";
   }

    //最长字段长度 6
    private int filedPad = 6;

    @Override
    public String toString(String indent) {
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("CSChannelOfflineMessage").append("[1103]").append("{");
        //channel token
        sb.append("\n");
        sb.append(indent).append(rightPad("token", filedPad)).append(" = ").append(token);
        //用户Id
        sb.append("\n");
        sb.append(indent).append(rightPad("userId", filedPad)).append(" = ").append(userId);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}