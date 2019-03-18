package com.senpure.snowflake.result;

import com.senpure.base.result.ActionResult;
import com.senpure.base.result.Result;
import com.senpure.snowflake.model.ServerCenterAndWork;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Locale;

/**
 * WorkId
 *
 * @author senpure-generator
 * @version 2019-3-12 9:58:25
 */
public class ServerCenterAndWorkRecordResult extends ActionResult {
    private static final long serialVersionUID = 1562592198L;

    @ApiModelProperty(position = 3)
    private ServerCenterAndWork serverCenterAndWork;

    public static ServerCenterAndWorkRecordResult success() {
        return new ServerCenterAndWorkRecordResult(Result.SUCCESS);
    }

    public static ServerCenterAndWorkRecordResult dim() {
        return new ServerCenterAndWorkRecordResult(Result.ERROR_DIM);
    }

    public static ServerCenterAndWorkRecordResult failure() {
        return new ServerCenterAndWorkRecordResult(Result.FAILURE);
    }

    public static ServerCenterAndWorkRecordResult notExist() {
        return new ServerCenterAndWorkRecordResult(Result.TARGET_NOT_EXIST);
    }

    public static ServerCenterAndWorkRecordResult result(int code) {
        return new ServerCenterAndWorkRecordResult(code);
    }

    public ServerCenterAndWorkRecordResult() {
    }

    public ServerCenterAndWorkRecordResult(int code) {
        super(code);
    }

    public ServerCenterAndWork getServerCenterAndWork() {
        return serverCenterAndWork;
    }

    public ServerCenterAndWorkRecordResult setServerCenterAndWork(ServerCenterAndWork serverCenterAndWork) {
        this.serverCenterAndWork = serverCenterAndWork;
        return this;
    }

    @Override
    public ServerCenterAndWorkRecordResult setClientFormat(boolean clientFormat) {
        super.setClientFormat(clientFormat);
        return this;
    }

    @Override
    public ServerCenterAndWorkRecordResult setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    @Override
    public ServerCenterAndWorkRecordResult setMessageArgs(List<String> messageArgs) {
        super.setMessageArgs(messageArgs);
        return this;
    }

    @Override
    public ServerCenterAndWorkRecordResult wrapMessage(Locale locale) {
        super.wrapMessage(locale);
        return this;
    }

    @Override
    public ServerCenterAndWorkRecordResult wrapMessage(Locale locale, Object... args) {
        super.wrapMessage(locale, args);
        return this;
    }
}