package com.senpure.snowflake.criteria;

import com.senpure.base.criterion.CriteriaStr;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * SnowflakeLock
 *
 * @author senpure-generator
 * @version 2019-3-12 13:43:04
 */
public class SnowflakeLockCriteriaStr extends CriteriaStr implements Serializable {
    private static final long serialVersionUID = 0L;

    //(主键)
    @ApiModelProperty(value = "(主键)", dataType = "long", example = "666666", position = 1)
    private String id;
    //乐观锁，版本控制
    @ApiModelProperty(hidden = true )
    private String version;

    public SnowflakeLockCriteria toSnowflakeLockCriteria() {
        SnowflakeLockCriteria criteria = new SnowflakeLockCriteria();
        criteria.setPage(Integer.valueOf(getPage()));
        criteria.setPageSize(Integer.valueOf(getPageSize()));
        //(主键)
        if (id != null) {
            criteria.setId(Long.valueOf(id));
        }
        //乐观锁，版本控制
        if (version != null) {
            criteria.setVersion(Integer.valueOf(version));
        }
        return criteria;
    }

    @Override
    protected void beforeStr(StringBuilder sb) {
        sb.append("SnowflakeLockCriteriaStr{");
        if (id != null) {
            sb.append("id=").append(id).append(",");
        }
        if (version != null) {
            sb.append("version=").append(version).append(",");
        }
    }

    @Override
    protected void afterStr(StringBuilder sb) {
        super.afterStr(sb);
    }

    /**
     * get (主键)
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * set (主键)
     *
     * @return
     */
    public SnowflakeLockCriteriaStr setId(String id) {
        if (id != null && id.trim().length() == 0) {
            return this;
        }
        this.id = id;
        return this;
    }

    /**
     * get 乐观锁，版本控制
     *
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * set 乐观锁，版本控制
     *
     * @return
     */
    public SnowflakeLockCriteriaStr setVersion(String version) {
        if (version != null && version.trim().length() == 0) {
            return this;
        }
        this.version = version;
        return this;
    }

}