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
public class ServerCenterAndWorkPageResult extends ActionResult {
    private static final long serialVersionUID = 1562592198L;

    @ApiModelProperty(position = 3, value = "结果集总数", example = "1086")
    private int total;
    @ApiModelProperty(position = 4, value = "一页数据")
    private List<ServerCenterAndWork> serverCenterAndWorks ;

    public static ServerCenterAndWorkPageResult success() {
        return new ServerCenterAndWorkPageResult(Result.SUCCESS);
    }

    public static ServerCenterAndWorkPageResult dim() {
        return new ServerCenterAndWorkPageResult(Result.ERROR_DIM);
    }

    public static ServerCenterAndWorkPageResult failure() {
        return new ServerCenterAndWorkPageResult(Result.FAILURE);
    }

    public static ServerCenterAndWorkPageResult notExist() {
        return new ServerCenterAndWorkPageResult(Result.TARGET_NOT_EXIST);
    }

    public static ServerCenterAndWorkPageResult result(int code) {
        return new ServerCenterAndWorkPageResult(code);
    }

    public ServerCenterAndWorkPageResult() {
    }

    public ServerCenterAndWorkPageResult(int code) {
        super(code);
    }

    public int getTotal() {
        return total;
    }

    public ServerCenterAndWorkPageResult setTotal(int total) {
        this.total = total;
        return this;
    }

    public List<ServerCenterAndWork> getServerCenterAndWorks() {
        return serverCenterAndWorks;
    }

    public ServerCenterAndWorkPageResult setServerCenterAndWorks(List<ServerCenterAndWork> serverCenterAndWorks) {
        this.serverCenterAndWorks = serverCenterAndWorks;
        return this;
    }

    @Override
    public ServerCenterAndWorkPageResult setClientFormat(boolean clientFormat) {
        super.setClientFormat(clientFormat);
        return this;
    }

    @Override
    public ServerCenterAndWorkPageResult setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    @Override
    public ServerCenterAndWorkPageResult setMessageArgs(List<String> messageArgs) {
        super.setMessageArgs(messageArgs);
        return this;
    }

    @Override
    public ServerCenterAndWorkPageResult wrapMessage(Locale locale) {
        super.wrapMessage(locale);
        return this;
    }

    @Override
    public ServerCenterAndWorkPageResult wrapMessage(Locale locale, Object... args) {
        super.wrapMessage(locale, args);
        return this;
    }
}