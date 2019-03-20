package com.senpure.io.message;

import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;

/**
 * 心跳
 * 
 * @author senpure
 * @time 2019-3-20 16:36:02
 */
public class CSHeartMessage extends  Message {

    public static final int MESSAGE_ID = 1301;
    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf){
        getSerializedSize();
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
        serializedSize = size ;
        return size ;
    }


    @Override
    public int getMessageId() {
        return 1301;
    }

    @Override
    public String toString() {
        return "CSHeartMessage[1301]{"
                + "}";
   }


    @Override
    public String toString(String indent) {
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("CSHeartMessage").append("[1301]").append("{");
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}