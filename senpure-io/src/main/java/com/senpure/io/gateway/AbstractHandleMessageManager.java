package com.senpure.io.gateway;

/**
 * AbstractHandleMessageManager
 *
 * @author senpure
 * @time 2018-10-25 15:13:28
 */
public abstract class AbstractHandleMessageManager {

    private int messageType;
    private long numStart;
    private long numEnd;
    private int valueType;
    private boolean serverShare;


    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getNumStart() {
        return numStart;
    }

    public void setNumStart(long numStart) {
        this.numStart = numStart;
    }

    public long getNumEnd() {
        return numEnd;
    }

    public void setNumEnd(long numEnd) {
        this.numEnd = numEnd;
    }

    public int getValueType() {
        return valueType;
    }

    public void setValueType(int valueType) {
        this.valueType = valueType;
    }

    public boolean isServerShare() {
        return serverShare;
    }

    public void setServerShare(boolean serverShare) {
        this.serverShare = serverShare;
    }
}
