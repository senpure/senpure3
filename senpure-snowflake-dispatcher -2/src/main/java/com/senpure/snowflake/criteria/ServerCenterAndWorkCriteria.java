package com.senpure.snowflake.criteria;

import com.senpure.base.criterion.Criteria;
import com.senpure.snowflake.model.ServerCenterAndWork;

import java.io.Serializable;

/**
 * WorkId
 *
 * @author senpure-generator
 * @version 2019-3-12 9:58:25
 */
public class ServerCenterAndWorkCriteria extends Criteria implements Serializable {
    private static final long serialVersionUID = 1562592198L;

    //(主键)
    private Long id;
    //乐观锁，版本控制
    private Integer version;
    private String serverName;
    //table [server_center_and_work][column = server_name] criteriaOrder
    private String serverNameOrder;
    private String serverKey;
    //table [server_center_and_work][column = server_key] criteriaOrder
    private String serverKeyOrder;
    private Integer centerId;
    //table [server_center_and_work][column = center_id] criteriaOrder
    private String centerIdOrder;
    private Integer workId;
    //table [server_center_and_work][column = work_id] criteriaOrder
    private String workIdOrder;

    public static ServerCenterAndWork toServerCenterAndWork(ServerCenterAndWorkCriteria criteria, ServerCenterAndWork serverCenterAndWork) {
        serverCenterAndWork.setId(criteria.getId());
        serverCenterAndWork.setServerName(criteria.getServerName());
        serverCenterAndWork.setServerKey(criteria.getServerKey());
        serverCenterAndWork.setCenterId(criteria.getCenterId());
        serverCenterAndWork.setWorkId(criteria.getWorkId());
        serverCenterAndWork.setVersion(criteria.getVersion());
        return serverCenterAndWork;
    }

    public ServerCenterAndWork toServerCenterAndWork() {
        ServerCenterAndWork serverCenterAndWork = new ServerCenterAndWork();
        return toServerCenterAndWork(this, serverCenterAndWork);
    }

    /**
     * 将ServerCenterAndWorkCriteria 的有效值(不为空),赋值给 ServerCenterAndWork
     *
     * @return ServerCenterAndWork
     */
    public ServerCenterAndWork effective(ServerCenterAndWork serverCenterAndWork) {
        if (getId() != null) {
            serverCenterAndWork.setId(getId());
        }
        if (getServerName() != null) {
            serverCenterAndWork.setServerName(getServerName());
        }
        if (getServerKey() != null) {
            serverCenterAndWork.setServerKey(getServerKey());
        }
        if (getCenterId() != null) {
            serverCenterAndWork.setCenterId(getCenterId());
        }
        if (getWorkId() != null) {
            serverCenterAndWork.setWorkId(getWorkId());
        }
        if (getVersion() != null) {
            serverCenterAndWork.setVersion(getVersion());
        }
        return serverCenterAndWork;
    }

    @Override
    protected void beforeStr(StringBuilder sb) {
        sb.append("ServerCenterAndWorkCriteria{");
        if (id != null) {
            sb.append("id=").append(id).append(",");
        }
        if (version != null) {
            sb.append("version=").append(version).append(",");
        }
        if (serverName != null) {
            sb.append("serverName=").append(serverName).append(",");
        }
        if (serverKey != null) {
            sb.append("serverKey=").append(serverKey).append(",");
        }
        if (centerId != null) {
            sb.append("centerId=").append(centerId).append(",");
        }
        if (workId != null) {
            sb.append("workId=").append(workId).append(",");
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
    public ServerCenterAndWorkCriteria setId(Long id) {
        this.id = id;
        return this;
    }

    public String getServerName() {
        return serverName;
    }


    public ServerCenterAndWorkCriteria setServerName(String serverName) {
        if (serverName != null && serverName.trim().length() == 0) {
            this.serverName = null;
            return this;
        }
        this.serverName = serverName;
        return this;
    }

    /**
     * get table [server_center_and_work][column = server_name] criteriaOrder
     *
     * @return
     */
    public String getServerNameOrder() {
        return serverNameOrder;
    }

    /**
     * set table [server_center_and_work][column = server_name] criteriaOrder DESC||ASC
     *
     * @return
     */
    public ServerCenterAndWorkCriteria setServerNameOrder(String serverNameOrder) {
        this.serverNameOrder = serverNameOrder;
        putSort("server_name", serverNameOrder);
        return this;
    }

    public String getServerKey() {
        return serverKey;
    }


    public ServerCenterAndWorkCriteria setServerKey(String serverKey) {
        if (serverKey != null && serverKey.trim().length() == 0) {
            this.serverKey = null;
            return this;
        }
        this.serverKey = serverKey;
        return this;
    }

    /**
     * get table [server_center_and_work][column = server_key] criteriaOrder
     *
     * @return
     */
    public String getServerKeyOrder() {
        return serverKeyOrder;
    }

    /**
     * set table [server_center_and_work][column = server_key] criteriaOrder DESC||ASC
     *
     * @return
     */
    public ServerCenterAndWorkCriteria setServerKeyOrder(String serverKeyOrder) {
        this.serverKeyOrder = serverKeyOrder;
        putSort("server_key", serverKeyOrder);
        return this;
    }

    public Integer getCenterId() {
        return centerId;
    }


    public ServerCenterAndWorkCriteria setCenterId(Integer centerId) {
        this.centerId = centerId;
        return this;
    }

    /**
     * get table [server_center_and_work][column = center_id] criteriaOrder
     *
     * @return
     */
    public String getCenterIdOrder() {
        return centerIdOrder;
    }

    /**
     * set table [server_center_and_work][column = center_id] criteriaOrder DESC||ASC
     *
     * @return
     */
    public ServerCenterAndWorkCriteria setCenterIdOrder(String centerIdOrder) {
        this.centerIdOrder = centerIdOrder;
        putSort("center_id", centerIdOrder);
        return this;
    }

    public Integer getWorkId() {
        return workId;
    }


    public ServerCenterAndWorkCriteria setWorkId(Integer workId) {
        this.workId = workId;
        return this;
    }

    /**
     * get table [server_center_and_work][column = work_id] criteriaOrder
     *
     * @return
     */
    public String getWorkIdOrder() {
        return workIdOrder;
    }

    /**
     * set table [server_center_and_work][column = work_id] criteriaOrder DESC||ASC
     *
     * @return
     */
    public ServerCenterAndWorkCriteria setWorkIdOrder(String workIdOrder) {
        this.workIdOrder = workIdOrder;
        putSort("work_id", workIdOrder);
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
    public ServerCenterAndWorkCriteria setVersion(Integer version) {
        this.version = version;
        return this;
    }

}