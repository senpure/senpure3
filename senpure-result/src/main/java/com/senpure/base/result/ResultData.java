package com.senpure.base.result;

/**
 * ResultData
 *
 * @author senpure
 * @time 2018-12-28 17:42:26
 */
public class ResultData<T> {
    private int code;
    private String message;
    private T data;

    public boolean isSuccess() {

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
