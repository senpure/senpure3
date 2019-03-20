package com.senpure.io.message;

import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;

/**
 * 踢下线
 * 
 * @author senpure
 * @time 2019-3-20 16:36:02
 */
public class SCKickOffMessage extends  Message {

    public static final int MESSAGE_ID = 1204;
    //token
    private long token;
    //userId
    private long userId;
    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf){
        getSerializedSize();
        //token
        writeVar64(buf,8,token);
        //userId
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
                //token
                case 8:// 1 << 3 | 0
                        token = readVar64(buf);
                    break;
                //userId
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
        //token
        size += computeVar64Size(1,token);
        //userId
        size += computeVar64Size(1,userId);
        serializedSize = size ;
        return size ;
    }

    /**
     * get token
     * @return
     */
    public  long getToken() {
        return token;
    }

    /**
     * set token
     */
    public SCKickOffMessage setToken(long token) {
        this.token=token;
        return this;
    }
    /**
     * get userId
     * @return
     */
    public  long getUserId() {
        return userId;
    }

    /**
     * set userId
     */
    public SCKickOffMessage setUserId(long userId) {
        this.userId=userId;
        return this;
    }

    @Override
    public int getMessageId() {
        return 1204;
    }

    @Override
    public String toString() {
        return "SCKickOffMessage[1204]{"
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
        sb.append("SCKickOffMessage").append("[1204]").append("{");
        //token
        sb.append("\n");
        sb.append(indent).append(rightPad("token", filedPad)).append(" = ").append(token);
        //userId
        sb.append("\n");
        sb.append(indent).append(rightPad("userId", filedPad)).append(" = ").append(userId);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}