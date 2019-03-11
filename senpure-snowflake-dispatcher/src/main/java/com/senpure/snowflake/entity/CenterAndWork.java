package com.senpure.snowflake.entity;

import com.senpure.base.entity.LongAndVersionEntity;

/**
 * WorkId
 *
 * @author senpure
 * @time 2019-03-11 18:02:59
 */
public class CenterAndWork  extends LongAndVersionEntity  {

    private String serverName;
    private String serverKey;
    private int centerId;
    private  int workId;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public int getCenterId() {
        return centerId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }
}
