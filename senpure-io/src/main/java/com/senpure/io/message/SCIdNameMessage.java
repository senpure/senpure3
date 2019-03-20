package com.senpure.io.message;

import com.senpure.io.bean.IdName;
import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.ArrayList;

/**
 * 数字id与字符串的关联
 * 
 * @author senpure
 * @time 2019-3-20 16:36:02
 */
public class SCIdNameMessage extends  Message {

    public static final int MESSAGE_ID = 1108;
    private List<IdName> idNames = new ArrayList(16);
    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf){
        getSerializedSize();
        for (int i= 0;i< idNames.size();i++){
            writeBean(buf,11,idNames.get(i));
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
                case 11:// 1 << 3 | 3
                        IdName tempIdNamesBean = new IdName();
                        readBean(buf,tempIdNamesBean);
                        idNames.add(tempIdNamesBean);
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
        for(int i=0;i< idNames.size();i++){
            size += computeBeanSize(1,idNames.get(i));
        }
        serializedSize = size ;
        return size ;
    }

    public List<IdName> getIdNames(){
        return idNames;
    }
    public SCIdNameMessage setIdNames (List<IdName> idNames){
        if(idNames == null){
        this.idNames = new ArrayList(16);
            return this;
        }
        this.idNames=idNames;
        return this;
    }


    @Override
    public int getMessageId() {
        return 1108;
    }

    @Override
    public String toString() {
        return "SCIdNameMessage[1108]{"
                +"idNames=" + idNames
                + "}";
   }

    //7 + 3 = 10 个空格
    private String nextIndent ="          ";
    //最长字段长度 7
    private int filedPad = 7;

    @Override
    public String toString(String indent) {
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCIdNameMessage").append("[1108]").append("{");
        sb.append("\n");
        sb.append(indent).append(rightPad("idNames", filedPad)).append(" = ");
        int idNamesSize = idNames.size();
        if (idNamesSize > 0) {
            sb.append("[");
            for (int i = 0; i<idNamesSize;i++) {
                sb.append("\n");
                sb.append(nextIndent);
                sb.append(indent).append(idNames.get(i).toString(indent + nextIndent));
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