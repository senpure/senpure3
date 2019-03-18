package com.senpure.snowflake.criteria;

import com.senpure.base.criterion.Criteria;
import com.senpure.snowflake.model.SnowflakeLock;

import java.io.Serializable;

/**
 * SnowflakeLock
 *
 * @author senpure-generator
 * @version 2019-3-12 13:43:04
 */
public class SnowflakeLockCriteria extends Criteria implements Serializable {
    private static final long serialVersionUID = 0L;

    //(主键)
    private Long id;
    //乐观锁，版本控制
    private Integer version;

    public static SnowflakeLock toSnowflakeLock(SnowflakeLockCriteria criteria, SnowflakeLock snowflakeLock) {
        snowflakeLock.setId(criteria.getId());
        snowflakeLock.setVersion(criteria.getVersion());
        return snowflakeLock;
    }

    public SnowflakeLock toSnowflakeLock() {
        SnowflakeLock snowflakeLock = new SnowflakeLock();
        return toSnowflakeLock(this, snowflakeLock);
    }

    /**
     * 将SnowflakeLockCriteria 的有效值(不为空),赋值给 SnowflakeLock
     *
     * @return SnowflakeLock
     */
    public SnowflakeLock effective(SnowflakeLock snowflakeLock) {
        if (getId() != null) {
            snowflakeLock.setId(getId());
        }
        if (getVersion() != null) {
            snowflakeLock.setVersion(getVersion());
        }
        return snowflakeLock;
    }

    @Override
    protected void beforeStr(StringBuilder sb) {
        sb.append("SnowflakeLockCriteria{");
        if (id != null) {
            sb.append("id=").append(id).append(",");
        }
        if (version != null) {
            sb.append("version=").append(version).append(",");
        }
    }

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
    public SnowflakeLockCriteria setId(Long id) {
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
    public SnowflakeLockCriteria setVersion(Integer version) {
        this.version = version;
        return this;
    }

}