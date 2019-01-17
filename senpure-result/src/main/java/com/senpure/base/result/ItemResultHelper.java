package com.senpure.base.result;

/**
 * ItemResultHelper
 *
 * @author senpure
 * @time 2019-01-17 16:24:01
 */
public class ItemResultHelper {

    public static  <T>ItemResult<T> success() {
        return new ItemResult(Result.SUCCESS);
    }

    public static <T>ItemResult<T>  dim() {
        return new ItemResult(Result.ERROR_DIM);
    }

    public static <T>ItemResult<T>  failure() {
        return new ItemResult(Result.FAILURE);
    }

    public static <T>ItemResult<T>  notExist() {
        return new ItemResult(Result.TARGET_NOT_EXIST);
    }

    public static <T>ItemResult<T>  result(int code) {
        return new ItemResult(code);
    }


    public static void main(String[] args) {
        ItemResult<Result> itemResult = ItemResultHelper.success();

        System.out.println(itemResult);
    }
}
