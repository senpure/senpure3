package com.senpure.base.result;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * PageResult
 *
 * @author senpure
 * @time 2019-01-02 11:20:27
 */
public class PageResult<T> extends ActionResult {
    @ApiModelProperty(position = 3, value = "结果集总数", example = "1086")
    private int total;
    @ApiModelProperty(position = 4, value = "一页数据")
    private List<T> items;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
