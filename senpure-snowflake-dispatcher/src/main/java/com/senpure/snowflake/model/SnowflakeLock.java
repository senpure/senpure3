package com.senpure.snowflake.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * SnowflakeLock
 * 
 * @author senpure-generator
 * @version 2019-3-12 13:43:04
 */
@ApiModel(description = "SnowflakeLock")
public class SnowflakeLock implements Serializable {
    private static final long serialVersionUID = 0L;

    //(主键)
    private Long id;
    //乐观锁，版本控制
    @ApiModelProperty(hidden = true )
    private Integer version;

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
    public SnowflakeLock setId(Long id) {
        this.id = id;
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
    public SnowflakeLock setVersion(Integer version) {
        this.version = version;
        return this;
    }


    @Override
    public String toString() {
        return "SnowflakeLock{"
                + "id=" + id
                + ",version=" + version
                + "}";
    }

}