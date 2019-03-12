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
public class SnowflakeLockRecordResult extends ActionResult {
    private static final long serialVersionUID = 0L;

    public static final String RECOR_DNAME = "snowflakeLock";

    @ApiModelProperty(position = 3)
    private SnowflakeLock snowflakeLock;

    public static SnowflakeLockRecordResult success() {
        return new SnowflakeLockRecordResult(Result.SUCCESS);
    }

    public static SnowflakeLockRecordResult dim() {
        return new SnowflakeLockRecordResult(Result.ERROR_DIM);
    }

    public static SnowflakeLockRecordResult failure() {
        return new SnowflakeLockRecordResult(Result.FAILURE);
    }

    public static SnowflakeLockRecordResult notExist() {
        return new SnowflakeLockRecordResult(Result.TARGET_NOT_EXIST);
    }

    public static SnowflakeLockRecordResult result(int code) {
        return new SnowflakeLockRecordResult(code);
    }

    public SnowflakeLockRecordResult() {
    }

    public SnowflakeLockRecordResult(int code) {
        super(code);
    }

    public SnowflakeLock getSnowflakeLock() {
        return snowflakeLock;
    }

    public SnowflakeLockRecordResult setSnowflakeLock(SnowflakeLock snowflakeLock) {
        this.snowflakeLock = snowflakeLock;
        return this;
    }

    @Override
    public SnowflakeLockRecordResult setClientFormat(boolean clientFormat) {
        super.setClientFormat(clientFormat);
        return this;
    }

    @Override
    public SnowflakeLockRecordResult setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    @Override
    public SnowflakeLockRecordResult setMessageArgs(List<String> messageArgs) {
        super.setMessageArgs(messageArgs);
        return this;
    }

    @Override
    public SnowflakeLockRecordResult wrapMessage(Locale locale) {
        super.wrapMessage(locale);
        return this;
    }

    @Override
    public SnowflakeLockRecordResult wrapMessage(Locale locale, Object... args) {
        super.wrapMessage(locale, args);
        return this;
    }
}