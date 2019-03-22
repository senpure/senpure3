package com.senpure.io.protocol;


/**
 * Constant
 *
 * @author senpure
 * @time 2019-02-20 15:31:42
 */
public class Constant {

    public static final int WIRETYPE_VARINT = 0;
    public static final int WIRETYPE_FIXED32 = 1;
    public static final int WIRETYPE_FIXED64 = 2;
    public static final int WIRETYPE_LENGTH_DELIMITED = 3;

    /**
     * 没有找到服务器
     */
    public final static String ERROR_NOT_FOUND_SERVER = "NOT_FOUND_SERVER";
    /**
     * 无法处理该请求
     */
    public final static String ERROR_NOT_HANDLE_REQUEST = "NOT_HANDLE_REQUEST";
    /**
     * 服务器错误
     */
    public final static String ERROR_SERVER_ERROR = "SERVER_ERROR ";


    public static final String BREAK_TYPE_ERROR = "ERROR";
    public static final String BREAK_TYPE_USER_OFFlINE = "USER_OFFLINE";
    public static final String BREAK_TYPE_USER_CHANGE = "USER_CHANGE";
}
