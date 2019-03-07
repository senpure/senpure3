package com.senpure.base.result;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * ActionResult
 *
 * @author senpure
 * @time 2019-01-02 10:47:27
 */
public class ActionResult implements Serializable {

    private static final long serialVersionUID = -2900736949472602435L;

    @ApiModelProperty(value = "操作结果 1 表示成功", example = "1")
    private int code;
    @ApiModelProperty(position = 1, value = "操作提示", example = "成功", allowEmptyValue = true)
    private String message;
    @ApiModelProperty(hidden = true, position = 100, value = "操作提示的格式格式化参数(一般是数字)", example = "[\"100000\",\"20000\"]", allowEmptyValue = true)
    protected List<String> messageArgs;

    @ApiModelProperty(hidden = true, position = 101)
    Map<String, String> validators = new HashMap<>();
    //客服端格式化
    @ApiModelProperty(hidden = true, position = 102)
    protected boolean clientFormat;


    public static ActionResult success() {
        return new ActionResult(Result.SUCCESS);
    }

    public static ActionResult dim() {
        return new ActionResult(Result.ERROR_DIM);
    }

    public static ActionResult failure() {
        return new ActionResult(Result.FAILURE);
    }

    public static ActionResult notExist() {
        return new ActionResult(Result.TARGET_NOT_EXIST);
    }

    public static ActionResult result(int code) {
        return new ActionResult(code);
    }

    public ActionResult(int code) {
        this.code = code;
    }

    public ActionResult() {
    }

    public ActionResult setClientFormat(boolean clientFormat) {
        this.clientFormat = clientFormat;
        return this;
    }

    public ActionResult wrapMessage(Locale locale) {
        if (messageArgs != null && !clientFormat) {
            setMessage(ResultHelper.getMessage(getCode(), locale, messageArgs.toArray()));

        } else {
            setMessage(ResultHelper.getMessage(getCode(), locale));
        }

        return this;
    }

    public ActionResult wrapMessage(Locale locale, Object... args) {
        setMessage(ResultHelper.getMessage(getCode(), locale));
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public ActionResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<String> getMessageArgs() {
        return messageArgs;
    }

    public ActionResult setMessageArgs(List<String> messageArgs) {
        this.messageArgs = messageArgs;
        return this;
    }

    public Map<String, String> getValidators() {
        return validators;
    }

    public ActionResult setValidators(Map<String, String> validators) {
        this.validators = validators;
        return this;
    }


    public static void main(String[] args) {

    }
}
