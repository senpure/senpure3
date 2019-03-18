package com.senpure.snowflake.result;

import com.senpure.base.result.ActionResult;
import com.senpure.base.result.Result;
import com.senpure.snowflake.model.SnowflakeLock;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Locale;

/**
 * SnowflakeLock
 *
 * @author senpure-generator
 * @version 2019-3-12 13:43:04
 */
public class SnowflakeLockPageResult extends ActionResult {
    private static final long serialVersionUID = 0L;

    public static final String RECORDS_NAME = "snowflakeLocks";

    @ApiModelProperty(position = 3, value = "结果集总数", example = "1086")
    private int total;
    @ApiModelProperty(position = 4, value = "一页数据")
    private List<SnowflakeLock> snowflakeLocks ;

    public static SnowflakeLockPageResult success() {
        return new SnowflakeLockPageResult(Result.SUCCESS);
    }

    public static SnowflakeLockPageResult dim() {
        return new SnowflakeLockPageResult(Result.ERROR_DIM);
    }

    public static SnowflakeLockPageResult failure() {
        return new SnowflakeLockPageResult(Result.FAILURE);
    }

    public static SnowflakeLockPageResult notExist() {
        return new SnowflakeLockPageResult(Result.TARGET_NOT_EXIST);
    }

    public static SnowflakeLockPageResult result(int code) {
        return new SnowflakeLockPageResult(code);
    }

    public SnowflakeLockPageResult() {
    }

    public SnowflakeLockPageResult(int code) {
        super(code);
    }

    public int getTotal() {
        return total;
    }

    public SnowflakeLockPageResult setTotal(int total) {
        this.total = total;
        return this;
    }

    public List<SnowflakeLock> getSnowflakeLocks() {
        return snowflakeLocks;
    }

    public SnowflakeLockPageResult setSnowflakeLocks(List<SnowflakeLock> snowflakeLocks) {
        this.snowflakeLocks = snowflakeLocks;
        return this;
    }

    @Override
    public SnowflakeLockPageResult setClientFormat(boolean clientFormat) {
        super.setClientFormat(clientFormat);
        return this;
    }

    @Override
    public SnowflakeLockPageResult setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    @Override
    public SnowflakeLockPageResult setMessageArgs(List<String> messageArgs) {
        super.setMessageArgs(messageArgs);
        return this;
    }

    @Override
    public SnowflakeLockPageResult wrapMessage(Locale locale) {
        super.wrapMessage(locale);
        return this;
    }

    @Override
    public SnowflakeLockPageResult wrapMessage(Locale locale, Object... args) {
        super.wrapMessage(locale, args);
        return this;
    }
}