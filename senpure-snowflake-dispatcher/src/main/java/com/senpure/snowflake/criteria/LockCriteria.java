package com.senpure.snowflake.criteria;

import com.senpure.base.criterion.Criteria;
import com.senpure.snowflake.model.Lock;

import java.io.Serializable;

/**
 * Lock
 *
 * @author senpure-generator
 * @version 2019-3-11 17:30:15
 */
public class LockCriteria extends Criteria implements Serializable {
    private static final long serialVersionUID = 0L;

    //(主键)
    private Long id;
    //乐观锁，版本控制
    private Integer version;

    public static Lock toLock(LockCriteria criteria, Lock lock) {
        lock.setId(criteria.getId());
        lock.setVersion(criteria.getVersion());
        return lock;
    }

    public Lock toLock() {
        Lock lock = new Lock();
        return toLock(this, lock);
    }

    /**
     * 将LockCriteria 的有效值(不为空),赋值给 Lock
     *
     * @return Lock
     */
    public Lock effective(Lock lock) {
        if (getId() != null) {
            lock.setId(getId());
        }
        if (getVersion() != null) {
            lock.setVersion(getVersion());
        }
        return lock;
    }

    @Override
    protected void beforeStr(StringBuilder sb) {
        sb.append("LockCriteria{");
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
    public LockCriteria setId(Long id) {
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
    public LockCriteria setVersion(Integer version) {
        this.version = version;
        return this;
    }

}