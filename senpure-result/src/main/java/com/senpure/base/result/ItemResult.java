package com.senpure.base.result;

import io.swagger.annotations.ApiModelProperty;

import java.awt.print.Book;
import java.util.List;
import java.util.Locale;

/**
 * ItemResult
 *
 * @author senpure
 * @time 2018-12-28 17:42:26
 */
public class ItemResult<T> extends ActionResult {
    private static final long serialVersionUID = 4750001063439986482L;

    @ApiModelProperty(position = 3)
    private T item;

    public static ItemResult success() {
        return new ItemResult(Result.SUCCESS);
    }

    public static ItemResult dim() {
        return new ItemResult(Result.ERROR_DIM);
    }

    public static ItemResult failure() {
        return new ItemResult(Result.FAILURE);
    }

    public static ItemResult notExist() {
        return new ItemResult(Result.TARGET_NOT_EXIST);
    }

    public static ItemResult result(int code) {
        return new ItemResult(code);
    }


    public ItemResult() {
    }

    public ItemResult(int code) {
        super(code);
    }

    @Override
    public ItemResult<T> setClientFormat(boolean clientFormat) {
        super.setClientFormat(clientFormat);
        return this;
    }


    @Override
    public ItemResult<T> setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    @Override
    public ItemResult<T> setMessageArgs(List<String> messageArgs) {
        super.setMessageArgs(messageArgs);
        return this;
    }

    @Override
    public ItemResult<T> wrapMessage(Locale locale) {
        super.wrapMessage(locale);
        return this;
    }

    @Override
    public ItemResult<T> wrapMessage(Locale locale, Object... args) {
        super.wrapMessage(locale, args);
        return this;
    }


    public T getItem() {
        return item;
    }

    public ItemResult<T> setItem(T item) {
        this.item = item;
        return this;
    }


    public static void main(String[] args) {


        ItemResult<Book> itemResult = ItemResultHelper.success();


    }
}
