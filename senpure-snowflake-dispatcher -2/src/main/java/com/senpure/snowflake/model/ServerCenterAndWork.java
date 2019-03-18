package com.senpure.snowflake.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * WorkId
 * 
 * @author senpure-generator
 * @version 2019-3-12 9:58:25
 */
@ApiModel(description = "WorkId")
public class ServerCenterAndWork implements Serializable {
    private static final long serialVersionUID = 1562592198L;

    //(主键)
    private Long id;
    //乐观锁，版本控制
    @ApiModelProperty(hidden = true )
    private Integer version;
    @ApiModelProperty(example = "serverName", position = 1)
    private String serverName;
    @ApiModelProperty(example = "serverKey", position = 2)
    private String serverKey;
    @ApiModelProperty(dataType = "int", example = "666666", position = 3)
    private Integer centerId;
    @ApiModelProperty(dataType = "int", example = "666666", position = 4)
    private Integer workId;

    /**
     * get (主键)
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * set (主键)
     *
     * @return
     */
    public ServerCenterAndWork setId(Long id) {
        this.id = id;
        return this;
    }


    public String getServerName() {
        return serverName;
    }


    public ServerCenterAndWork setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }


    public String getServerKey() {
        return serverKey;
    }


    public ServerCenterAndWork setServerKey(String serverKey) {
        this.serverKey = serverKey;
        return this;
    }


    public Integer getCenterId() {
        return centerId;
    }


    public ServerCenterAndWork setCenterId(Integer centerId) {
        this.centerId = centerId;
        return this;
    }


    public Integer getWorkId() {
        return workId;
    }


    public ServerCenterAndWork setWorkId(Integer workId) {
        this.workId = workId;
        return this;
    }


    /**
     * get 乐观锁，版本控制
     *
     * @return
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * set 乐观锁，版本控制
     *
     * @return
     */
    public ServerCenterAndWork setVersion(Integer version) {
        this.version = version;
        return this;
    }


    @Override
    public String toString() {
        return "ServerCenterAndWork{"
                + "id=" + id
                + ",version=" + version
                + ",serverName=" + serverName
                + ",serverKey=" + serverKey
                + ",centerId=" + centerId
                + ",workId=" + workId
                + "}";
    }

}