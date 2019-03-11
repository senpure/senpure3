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
public class LockRecordResult extends ActionResult {
    private static final long serialVersionUID = 0L;

    @ApiModelProperty(position = 3)
    private Lock lock;

    public static LockRecordResult success() {
        return new LockRecordResult(Result.SUCCESS);
    }

    public static LockRecordResult dim() {
        return new LockRecordResult(Result.ERROR_DIM);
    }

    public static LockRecordResult failure() {
        return new LockRecordResult(Result.FAILURE);
    }

    public static LockRecordResult notExist() {
        return new LockRecordResult(Result.TARGET_NOT_EXIST);
    }

    public static LockRecordResult result(int code) {
        return new LockRecordResult(code);
    }

    public LockRecordResult() {
    }

    public LockRecordResult(int code) {
        super(code);
    }

    public Lock getLock() {
        return lock;
    }

    public LockRecordResult setLock(Lock lock) {
        this.lock = lock;
        return this;
    }

    @Override
    public LockRecordResult setClientFormat(boolean clientFormat) {
        super.setClientFormat(clientFormat);
        return this;
    }

    @Override
    public LockRecordResult setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    @Override
    public LockRecordResult setMessageArgs(List<String> messageArgs) {
        super.setMessageArgs(messageArgs);
        return this;
    }

    @Override
    public LockRecordResult wrapMessage(Locale locale) {
        super.wrapMessage(locale);
        return this;
    }

    @Override
    public LockRecordResult wrapMessage(Locale locale, Object... args) {
        super.wrapMessage(locale, args);
        return this;
    }
}