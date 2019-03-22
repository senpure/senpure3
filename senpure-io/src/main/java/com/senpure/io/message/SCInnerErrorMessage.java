package com.senpure.io.message;

import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;

/**
 * 服务器内部错误提示
 * 
 * @author senpure
 * @time 2019-3-22 10:34:46
 */
public class SCInnerErrorMessage extends  Message {

    public static final int MESSAGE_ID = 1600;
    //错误类型
    private String type;
    //提示内容
    private String message;
    //消息id
    private int id;
    private String value;
    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf){
        getSerializedSize();
        //错误类型
        if (type != null){
            writeString(buf,8,type);
        }
        //提示内容
        if (message != null){
            writeString(buf,16,message);
        }
        //消息id
        writeVar32(buf,24,id);
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
                //错误类型
                case 8:// 1 << 3 | 0
                        type = readString(buf);
                    break;
                //提示内容
                case 16:// 2 << 3 | 0
                        message = readString(buf);
                    break;
                //消息id
                case 24:// 3 << 3 | 0
                        id = readVar32(buf);
                    break;
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
        //错误类型
        if (type != null){
            size += computeStringSize(1,type);
        }
        //提示内容
        if (message != null){
            size += computeStringSize(1,message);
        }
        //消息id
        size += computeVar32Size(1,id);
        if (value != null){
            size += computeStringSize(1,value);
        }
        serializedSize = size ;
        return size ;
    }

    /**
     * get 错误类型
     * @return
     */
    public  String getType() {
        return type;
    }

    /**
     * set 错误类型
     */
    public SCInnerErrorMessage setType(String type) {
        this.type=type;
        return this;
    }
    /**
     * get 提示内容
     * @return
     */
    public  String getMessage() {
        return message;
    }

    /**
     * set 提示内容
     */
    public SCInnerErrorMessage setMessage(String message) {
        this.message=message;
        return this;
    }
    /**
     * get 消息id
     * @return
     */
    public  int getId() {
        return id;
    }

    /**
     * set 消息id
     */
    public SCInnerErrorMessage setId(int id) {
        this.id=id;
        return this;
    }
    public  String getValue() {
        return value;
    }

    public SCInnerErrorMessage setValue(String value) {
        this.value=value;
        return this;
    }

    @Override
    public int getMessageId() {
        return 1600;
    }

    @Override
    public String toString() {
        return "SCInnerErrorMessage[1600]{"
                +"type=" + type
                +",message=" + message
                +",id=" + id
                +",value=" + value
                + "}";
   }

    //最长字段长度 7
    private int filedPad = 7;

    @Override
    public String toString(String indent) {
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCInnerErrorMessage").append("[1600]").append("{");
        //错误类型
        sb.append("\n");
        sb.append(indent).append(rightPad("type", filedPad)).append(" = ").append(type);
        //提示内容
        sb.append("\n");
        sb.append(indent).append(rightPad("message", filedPad)).append(" = ").append(message);
        //消息id
        sb.append("\n");
        sb.append(indent).append(rightPad("id", filedPad)).append(" = ").append(id);
        sb.append("\n");
        sb.append(indent).append(rightPad("value", filedPad)).append(" = ").append(value);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}