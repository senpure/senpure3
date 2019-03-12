package com.senpure.snowflake.criteria;

import com.senpure.base.criterion.CriteriaStr;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * WorkId
 *
 * @author senpure-generator
 * @version 2019-3-12 9:58:25
 */
public class ServerCenterAndWorkCriteriaStr extends CriteriaStr implements Serializable {
    private static final long serialVersionUID = 1562592198L;

    //(主键)
    @ApiModelProperty(value = "(主键)", dataType = "long", example = "666666", position = 5)
    private String id;
    //乐观锁，版本控制
    @ApiModelProperty(hidden = true)
    private String version;
    @NotEmpty
    @ApiModelProperty(example = "serverName", position = 6)
    private String serverName;
    @NotEmpty
    @ApiModelProperty(example = "serverKey", position = 7)
    private String serverKey;
    @ApiModelProperty(dataType = "int", example = "666666", position = 8)
    private String centerId;
    @ApiModelProperty(dataType = "int", example = "666666", position = 9)
    private String workId;
    //table [server_center_and_work][column = server_name] criteriaOrder
    @ApiModelProperty(value = "serverName 排序", allowableValues = "ASC,DESC", position = 10)
    private String serverNameOrder;
    //table [server_center_and_work][column = server_key] criteriaOrder
    @ApiModelProperty(value = "serverKey 排序", allowableValues = "ASC,DESC", position = 11)
    private String serverKeyOrder;
    //table [server_center_and_work][column = center_id] criteriaOrder
    @ApiModelProperty(value = "centerId 排序", allowableValues = "ASC,DESC", position = 12)
    private String centerIdOrder;
    //table [server_center_and_work][column = work_id] criteriaOrder
    @ApiModelProperty(value = "workId 排序", allowableValues = "ASC,DESC", position = 13)
    private String workIdOrder;

    public ServerCenterAndWorkCriteria toServerCenterAndWorkCriteria() {
        ServerCenterAndWorkCriteria criteria = new ServerCenterAndWorkCriteria();
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
        if (serverName != null) {
            criteria.setServerName(serverName);
        }
        //table [server_center_and_work][column = server_name] criteriaOrder
        if (serverNameOrder != null) {
            criteria.setServerNameOrder(serverNameOrder);
        }
        if (serverKey != null) {
            criteria.setServerKey(serverKey);
        }
        //table [server_center_and_work][column = server_key] criteriaOrder
        if (serverKeyOrder != null) {
            criteria.setServerKeyOrder(serverKeyOrder);
        }
        if (centerId != null) {
            criteria.setCenterId(Integer.valueOf(centerId));
        }
        //table [server_center_and_work][column = center_id] criteriaOrder
        if (centerIdOrder != null) {
            criteria.setCenterIdOrder(centerIdOrder);
        }
        if (workId != null) {
            criteria.setWorkId(Integer.valueOf(workId));
        }
        //table [server_center_and_work][column = work_id] criteriaOrder
        if (workIdOrder != null) {
            criteria.setWorkIdOrder(workIdOrder);
        }
        return criteria;
    }

    @Override
    protected void beforeStr(StringBuilder sb) {
        sb.append("ServerCenterAndWorkCriteriaStr{");
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

    @Override
    protected void afterStr(StringBuilder sb) {
        if (serverNameOrder != null) {
            sb.append("serverNameOrder=").append(serverNameOrder).append(",");
        }
        if (serverKeyOrder != null) {
            sb.append("serverKeyOrder=").append(serverKeyOrder).append(",");
        }
        if (centerIdOrder != null) {
            sb.append("centerIdOrder=").append(centerIdOrder).append(",");
        }
        if (workIdOrder != null) {
            sb.append("workIdOrder=").append(workIdOrder).append(",");
        }
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
    public ServerCenterAndWorkCriteriaStr setId(String id) {
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
    public ServerCenterAndWorkCriteriaStr setVersion(String version) {
        if (version != null && version.trim().length() == 0) {
            return this;
        }
        this.version = version;
        return this;
    }

    public String getServerName() {
        return serverName;
    }


    public ServerCenterAndWorkCriteriaStr setServerName(String serverName) {
        if (serverName != null && serverName.trim().length() == 0) {
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
    public ServerCenterAndWorkCriteriaStr setServerNameOrder(String serverNameOrder) {
        if (serverNameOrder != null && serverNameOrder.trim().length() == 0) {
            this.serverNameOrder = null;
            return this;
        }
        this.serverNameOrder = serverNameOrder;
        return this;
    }

    public String getServerKey() {
        return serverKey;
    }


    public ServerCenterAndWorkCriteriaStr setServerKey(String serverKey) {
        if (serverKey != null && serverKey.trim().length() == 0) {
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
    public ServerCenterAndWorkCriteriaStr setServerKeyOrder(String serverKeyOrder) {
        if (serverKeyOrder != null && serverKeyOrder.trim().length() == 0) {
            this.serverKeyOrder = null;
            return this;
        }
        this.serverKeyOrder = serverKeyOrder;
        return this;
    }

    public String getCenterId() {
        return centerId;
    }


    public ServerCenterAndWorkCriteriaStr setCenterId(String centerId) {
        if (centerId != null && centerId.trim().length() == 0) {
            return this;
        }
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
    public ServerCenterAndWorkCriteriaStr setCenterIdOrder(String centerIdOrder) {
        if (centerIdOrder != null && centerIdOrder.trim().length() == 0) {
            this.centerIdOrder = null;
            return this;
        }
        this.centerIdOrder = centerIdOrder;
        return this;
    }

    public String getWorkId() {
        return workId;
    }


    public ServerCenterAndWorkCriteriaStr setWorkId(String workId) {
        if (workId != null && workId.trim().length() == 0) {
            return this;
        }
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
    public ServerCenterAndWorkCriteriaStr setWorkIdOrder(String workIdOrder) {
        if (workIdOrder != null && workIdOrder.trim().length() == 0) {
            this.workIdOrder = null;
            return this;
        }
        this.workIdOrder = workIdOrder;
        return this;
    }

}