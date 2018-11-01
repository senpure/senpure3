package com.senpure.io.message;

import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;

/**
 * 断开用户与网关
 * 
 * @author senpure
 * @time 2018-11-1 15:06:13
 */
public class CSBreakUserGatewayMessage extends  Message {
    //channel token
    private long token;
    //用户Id
    private long userId;
    //relation token
    private long relationToken;
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
        //relation token
        writeVar64(buf,24,relationToken);
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
                //relation token
                case 24:// 3 << 3 | 0
                        relationToken = readVar64(buf);
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
        //relation token
        size += computeVar64Size(1,relationToken);
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
    public CSBreakUserGatewayMessage setToken(long token) {
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
    public CSBreakUserGatewayMessage setUserId(long userId) {
        this.userId=userId;
        return this;
    }
    /**
     * get relation token
     * @return
     */
    public  long getRelationToken() {
        return relationToken;
    }

    /**
     * set relation token
     */
    public CSBreakUserGatewayMessage setRelationToken(long relationToken) {
        this.relationToken=relationToken;
        return this;
    }

    @Override
    public int getMessageId() {
        return 1201;
    }

    @Override
    public String toString() {
        return "CSBreakUserGatewayMessage[1201]{"
                +"token=" + token
                +",userId=" + userId
                +",relationToken=" + relationToken
                + "}";
   }

    //最长字段长度 13
    private int filedPad = 13;

    @Override
    public String toString(String indent) {
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("CSBreakUserGatewayMessage").append("[1201]").append("{");
        //channel token
        sb.append("\n");
        sb.append(indent).append(rightPad("token", filedPad)).append(" = ").append(token);
        //用户Id
        sb.append("\n");
        sb.append(indent).append(rightPad("userId", filedPad)).append(" = ").append(userId);
        //relation token
        sb.append("\n");
        sb.append(indent).append(rightPad("relationToken", filedPad)).append(" = ").append(relationToken);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}