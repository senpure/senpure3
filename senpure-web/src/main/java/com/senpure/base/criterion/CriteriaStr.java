package com.senpure.base.criterion;


import com.senpure.base.struct.PatternDate;
import com.senpure.base.validator.DynamicDate;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;


public class CriteriaStr implements Serializable {

    public static final String ORDER_DESC = "DESC";
    public static final String ORDER_ASC = "ASC";

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private String startDate, endDate;

    @DynamicDate
    @ApiModelProperty(hidden = true)
    private PatternDate startDateValid = new PatternDate();
    @ApiModelProperty(hidden = true)
    @DynamicDate
    private PatternDate endDateValid = new PatternDate();
    @ApiModelProperty( hidden = true,position = 200, value = "页数", example = "2", dataType = "int")
    @Min(value = 1, message = "{input.error}")
    private String page = "1";
    @Min(value = 5, message = "{input.error}")
    @Max(value = 200, message = "{input.error}")
    @ApiModelProperty(hidden = true,position = 201, value = "每页数据", notes = "每页显示多少条数据，默认15条", example = "20",dataType = "int")
    private String pageSize = "15";
    @ApiModelProperty(hidden = true)
    private String datePattern = "yyyy-MM-dd HH:mm:ss";

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        if (startDate != null && startDate.trim().length() == 0) {
            startDate = null;
        }
        this.startDate = startDate;
        startDateValid.setDateStr(startDate);
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        if (endDate != null && endDate.trim().length() == 0) {
            endDate = null;
        }
        this.endDate = endDate;
        endDateValid.setDateStr(endDate);
    }


    protected PatternDate getStartDateValid() {
        return startDateValid;
    }


    protected PatternDate getEndDateValid() {
        return endDateValid;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        if (datePattern != null && datePattern.trim().length() == 0) {
            this.datePattern = null;
            return;
        }
        this.datePattern = datePattern;
        startDateValid.setPattern(datePattern);
        endDateValid.setPattern(datePattern);
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }


    public String getPageSize() {
        return pageSize;
    }


    public Criteria toCriteria() {
        Criteria criteria = new Criteria();
        criteria.setPage(Integer.valueOf(getPage()));
        criteria.setPageSize(Integer.valueOf(getPageSize()));
        criteria.setStartDate(startDateValid.getDate());
        criteria.setEndDate(endDateValid.getDate());
        return criteria;
    }

    protected void beforeStr(StringBuilder sb) {
        sb.append("Criteria{");
    }

    protected void rangeStr(StringBuilder sb) {
        String empty = "";
        sb.append(empty);

    }

    protected void dateStr(StringBuilder sb) {
        if (getStartDate() != null) {
            sb.append("startDate=").append(getStartDate()).append(",");
        }
        if (getEndDate() != null) {
            sb.append("endDate=").append(getEndDate()).append(",");
        }
    }

    protected void afterStr(StringBuilder sb) {
        sb.append("page=").append(getPage()).append(",");
        sb.append("pageSize=").append(getPageSize()).append(",");
        if (sb.length() > 2) {
            sb.delete(sb.length() - 1, sb.length());
        }
        sb.append("}");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        beforeStr(sb);
        rangeStr(sb);
        afterStr(sb);
        return sb.toString();
    }
}
