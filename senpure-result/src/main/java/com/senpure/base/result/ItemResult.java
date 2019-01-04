package com.senpure.base.result;

import io.swagger.annotations.ApiModelProperty;

/**
 * ItemResult
 *
 * @author senpure
 * @time 2018-12-28 17:42:26
 */
public class ItemResult<T> extends ActionResult {
    @ApiModelProperty(position = 3)
    private T item;

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
