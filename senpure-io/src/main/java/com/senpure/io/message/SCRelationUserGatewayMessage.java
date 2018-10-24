package com.senpure.io.message;

import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;

/**
 * 关联用户与网关
 * 
 * @author senpure
 * @time 2018-10-19 16:14:32
 */
public class SCRelationUserGatewayMessage extends  Message {
    //channel token
    private long token;
    //用户Id
    private long userId;
    //once token
    private long onceToken;
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
        //once token
        writeVar64(buf,24,onceToken);
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
                //once token
                case 24:// 3 << 3 | 0
                        onceToken = readVar64(buf);
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
        //once token
        size += computeVar64Size(1,onceToken);
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
    public SCRelationUserGatewayMessage setToken(long token) {
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
    public SCRelationUserGatewayMessage setUserId(long userId) {
        this.userId=userId;
        return this;
    }
    /**
     * get once token
     * @return
     */
    public  long getOnceToken() {
        return onceToken;
    }

    /**
     * set once token
     */
    public SCRelationUserGatewayMessage setOnceToken(long onceToken) {
        this.onceToken=onceToken;
        return this;
    }

    @Override
    public int getMessageId() {
        return 1102;
    }

    @Override
    public String toString() {
        return "SCRelationUserGatewayMessage[1102]{"
                +"token=" + token
                +",userId=" + userId
                +",onceToken=" + onceToken
                + "}";
   }

    //最长字段长度 9
    private int filedPad = 9;

    @Override
    public String toString(String indent) {
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCRelationUserGatewayMessage").append("[1102]").append("{");
        //channel token
        sb.append("\n");
        sb.append(indent).append(rightPad("token", filedPad)).append(" = ").append(token);
        //用户Id
        sb.append("\n");
        sb.append(indent).append(rightPad("userId", filedPad)).append(" = ").append(userId);
        //once token
        sb.append("\n");
        sb.append(indent).append(rightPad("onceToken", filedPad)).append(" = ").append(onceToken);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}