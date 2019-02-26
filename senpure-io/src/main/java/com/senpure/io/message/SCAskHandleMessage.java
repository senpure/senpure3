package com.senpure.io.message;

import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;

/**
 * 应答是否可以请求
 * 
 * @author senpure
 * @time 2019-2-20 17:02:08
 */
public class SCAskHandleMessage extends  Message {

    public static final int MESSAGE_ID = 1106;
    //是否可以处理
    private boolean handle;
    //token
    private long token;
    //消息ID
    private int fromMessageId;
    //值
    private String value;
    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf){
        getSerializedSize();
        //是否可以处理
        writeBoolean(buf,8,handle);
        //token
        writeVar64(buf,16,token);
        //消息ID
        writeVar32(buf,24,fromMessageId);
        //值
        if (value != null){
            writeString(buf,32,value);
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
                //是否可以处理
                case 8:// 1 << 3 | 0
                        handle = readBoolean(buf);
                    break;
                //token
                case 16:// 2 << 3 | 0
                        token = readVar64(buf);
                    break;
                //消息ID
                case 24:// 3 << 3 | 0
                        fromMessageId = readVar32(buf);
                    break;
                //值
                case 32:// 4 << 3 | 0
                        value = readString(buf);
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
        //是否可以处理
        size += computeBooleanSize(1,handle);
        //token
        size += computeVar64Size(1,token);
        //消息ID
        size += computeVar32Size(1,fromMessageId);
        //值
        if (value != null){
            size += computeStringSize(1,value);
        }
        serializedSize = size ;
        return size ;
    }

    /**
     *  is 是否可以处理
     * @return
     */
    public  boolean  isHandle() {
        return handle;
    }

    /**
     * set 是否可以处理
     */
    public SCAskHandleMessage setHandle(boolean handle) {
        this.handle=handle;
        return this;
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
    public SCAskHandleMessage setToken(long token) {
        this.token=token;
        return this;
    }
    /**
     * get 消息ID
     * @return
     */
    public  int getFromMessageId() {
        return fromMessageId;
    }

    /**
     * set 消息ID
     */
    public SCAskHandleMessage setFromMessageId(int fromMessageId) {
        this.fromMessageId=fromMessageId;
        return this;
    }
    public  String getValue() {
        return value;
    }

    public SCAskHandleMessage setValue(String value) {
        this.value=value;
        return this;
    }

    @Override
    public int getMessageId() {
        return 1106;
    }

    @Override
    public String toString() {
        return "SCAskHandleMessage[1106]{"
                +"handle=" + handle
                +",token=" + token
                +",fromMessageId=" + fromMessageId
                +",value=" + value
                + "}";
   }

    //最长字段长度 13
    private int filedPad = 13;

    @Override
    public String toString(String indent) {
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCAskHandleMessage").append("[1106]").append("{");
        //是否可以处理
        sb.append("\n");
        sb.append(indent).append(rightPad("handle", filedPad)).append(" = ").append(handle);
        //token
        sb.append("\n");
        sb.append(indent).append(rightPad("token", filedPad)).append(" = ").append(token);
        //消息ID
        sb.append("\n");
        sb.append(indent).append(rightPad("fromMessageId", filedPad)).append(" = ").append(fromMessageId);
        //值
        sb.append("\n");
        sb.append(indent).append(rightPad("value", filedPad)).append(" = ").append(value);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}