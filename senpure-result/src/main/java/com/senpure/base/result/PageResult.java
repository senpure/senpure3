package com.senpure.base.result;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Locale;

/**
 * PageResult
 *
 * @author senpure
 * @time 2019-01-02 11:20:27
 */
public class PageResult<T> extends ActionResult {
    private static final long serialVersionUID = 2438584673528847820L;

    @ApiModelProperty(position = 3, value = "结果集总数", example = "1086")
    private int total;
    @ApiModelProperty(position = 4, value = "一页数据")
    private List<T> items;

    public static PageResult success() {
        return new PageResult(Result.SUCCESS);
    }

    public static PageResult dim() {
        return new PageResult(Result.ERROR_DIM);
    }

    public static PageResult failure() {
        return new PageResult(Result.FAILURE);
    }

    public static PageResult notExist() {
        return new PageResult(Result.TARGET_NOT_EXIST);
    }

    public static PageResult result(int code) {
        return new PageResult(code);
    }


    public PageResult() {
    }

    public PageResult(int code) {
        super(code);
    }

    @Override
    public PageResult<T> setClientFormat(boolean clientFormat) {
        super.setClientFormat(clientFormat);
        return this;
    }


    @Override
    public PageResult<T> setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    @Override
    public PageResult<T> setMessageArgs(List<String> messageArgs) {
        super.setMessageArgs(messageArgs);
        return this;
    }

    @Override
    public PageResult<T> wrapMessage(Locale locale) {
        super.wrapMessage(locale);
        return this;
    }

    @Override
    public PageResult<T> wrapMessage(Locale locale, Object... args) {
        super.wrapMessage(locale, args);
        return this;
    }


    public int getTotal() {
        return total;
    }

    public PageResult<T> setTotal(int total) {
        this.total = total;
        return this;
    }

    public List<T> getItems() {
        return items;
    }

    public PageResult<T> setItems(List<T> items) {
        this.items = items;
        return this;
    }
}
