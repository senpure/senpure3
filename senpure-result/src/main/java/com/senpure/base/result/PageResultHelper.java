package com.senpure.base.result;

/**
 * PageResultHelper
 *
 * @author senpure
 * @time 2019-01-17 16:24:01
 */
public class PageResultHelper {

    public static <T> PageResult<T> success() {
        return new PageResult(Result.SUCCESS);
    }

    public static <T> PageResult<T> dim() {
        return new PageResult(Result.ERROR_DIM);
    }

    public static <T> PageResult<T> failure() {
        return new PageResult(Result.FAILURE);
    }

    public static <T> PageResult<T> notExist() {
        return new PageResult(Result.TARGET_NOT_EXIST);
    }

    public static <T> PageResult<T> result(int code) {
        return new PageResult(code);
    }


    public static void main(String[] args) {
        PageResult<Result> PageResult = PageResultHelper.success();

        System.out.println(PageResult);
    }
}
