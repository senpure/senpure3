package com.senpure.snowflake.entity;

import com.senpure.base.entity.VersionEntity;

import javax.persistence.*;

/**
 * WorkId
 *
 * @author senpure
 * @time 2019-03-11 18:02:59
 */
@Entity
public class ServerCenterAndWork extends VersionEntity {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String serverName;
    @Column(nullable = false)
    private String serverKey;
    @Column(nullable = false)
    private Integer centerId;
    @Column(nullable = false)
    private Integer workId;


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

    public Integer getCenterId() {
        return centerId;
    }

    public void setCenterId(Integer centerId) {
        this.centerId = centerId;
    }

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
