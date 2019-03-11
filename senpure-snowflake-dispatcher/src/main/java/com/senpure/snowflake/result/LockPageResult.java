package com.senpure.snowflake.result;

import com.senpure.base.result.ActionResult;
import com.senpure.base.result.Result;
import com.senpure.snowflake.model.Lock;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Locale;

/**
 * Lock
 *
 * @author senpure-generator
 * @version 2019-3-11 17:30:15
 */
public class LockPageResult extends ActionResult {
    private static final long serialVersionUID = 0L;

    @ApiModelProperty(position = 3, value = "结果集总数", example = "1086")
    private int total;
    @ApiModelProperty(position = 4, value = "一页数据")
    private List<Lock> locks ;

    public static LockPageResult success() {
        return new LockPageResult(Result.SUCCESS);
    }

    public static LockPageResult dim() {
        return new LockPageResult(Result.ERROR_DIM);
    }

    public static LockPageResult failure() {
        return new LockPageResult(Result.FAILURE);
    }

    public static LockPageResult notExist() {
        return new LockPageResult(Result.TARGET_NOT_EXIST);
    }

    public static LockPageResult result(int code) {
        return new LockPageResult(code);
    }

    public LockPageResult() {
    }

    public LockPageResult(int code) {
        super(code);
    }

    public int getTotal() {
        return total;
    }

    public LockPageResult setTotal(int total) {
        this.total = total;
        return this;
    }

    public List<Lock> getLocks() {
        return locks;
    }

    public LockPageResult setLocks(List<Lock> locks) {
        this.locks = locks;
        return this;
    }

    @Override
    public LockPageResult setClientFormat(boolean clientFormat) {
        super.setClientFormat(clientFormat);
        return this;
    }

    @Override
    public LockPageResult setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    @Override
    public LockPageResult setMessageArgs(List<String> messageArgs) {
        super.setMessageArgs(messageArgs);
        return this;
    }

    @Override
    public LockPageResult wrapMessage(Locale locale) {
        super.wrapMessage(locale);
        return this;
    }

    @Override
    public LockPageResult wrapMessage(Locale locale, Object... args) {
        super.wrapMessage(locale, args);
        return this;
    }
}