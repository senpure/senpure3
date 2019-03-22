package com.senpure.io.bean;

import com.senpure.io.protocol.Bean;
import io.netty.buffer.ByteBuf;

/**
 * @author senpure
 * @time 2019-3-22 10:34:46
 */
public class IdName extends  Bean {
    //消息id
    private int id;
    //有意义的字符串
    private String messageName;
    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf){
        getSerializedSize();
        //消息id
        writeVar32(buf,8,id);
        //有意义的字符串
        if (messageName != null){
            writeString(buf,16,messageName);
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
                //消息id
                case 8:// 1 << 3 | 0
                        id = readVar32(buf);
                    break;
                //有意义的字符串
                case 16:// 2 << 3 | 0
                        messageName = readString(buf);
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
        //消息id
        size += computeVar32Size(1,id);
        //有意义的字符串
        if (messageName != null){
            size += computeStringSize(1,messageName);
        }
        serializedSize = size ;
        return size ;
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
    public IdName setId(int id) {
        this.id=id;
        return this;
    }
    /**
     * get 有意义的字符串
     * @return
     */
    public  String getMessageName() {
        return messageName;
    }

    /**
     * set 有意义的字符串
     */
    public IdName setMessageName(String messageName) {
        this.messageName=messageName;
        return this;
    }

    @Override
    public String toString() {
        return "IdName{"
                +"id=" + id
                +",messageName=" + messageName
                + "}";
   }

    //最长字段长度 11
    private int filedPad = 11;

    @Override
    public String toString(String indent) {
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("IdName").append("{");
        //消息id
        sb.append("\n");
        sb.append(indent).append(rightPad("id", filedPad)).append(" = ").append(id);
        //有意义的字符串
        sb.append("\n");
        sb.append(indent).append(rightPad("messageName", filedPad)).append(" = ").append(messageName);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}