package com.senpure.base.result;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * ActionResult
 *
 * @author senpure
 * @time 2019-01-02 10:47:27
 */
public class ActionResult {

    @ApiModelProperty(value = "操作结果 1 表示成功", example = "1")
    private int code;
    @ApiModelProperty(position = 1, value = "操作提示", example = "您的权限不足", allowEmptyValue = true)
    private String message;
    @ApiModelProperty(position = 2, value = "操作提示的格式格式化参数(一般是数字)", example = "[\"100000\",\"20000\"]", allowEmptyValue = true)
    private List<String> messageArgs;

    public boolean success() {
        return code == Result.SUCCESS;
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

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getMessageArgs() {
        return messageArgs;
    }

    public void setMessageArgs(List<String> messageArgs) {
        this.messageArgs = messageArgs;
    }
}
