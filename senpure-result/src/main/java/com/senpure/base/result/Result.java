package com.senpure.base.result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;


public class Result {
    @Message("失败")
    public static final int FAILURE = 0;
    @Message("成功")
    public static final int SUCCESS = 1;

    @Message("未知错误")
    public static final int ERROR_UNKNOWN = 10;
    @Message("服务器发生错误")
    public static final int ERROR_SERVER = 11;
    @Message("服务器繁忙，请稍后再试")
    public static final int ERROR_DIM = 12;

    @Message("账号已经存在")
    public static final int ACCOUNT_ALREADY_EXISTS = 100;
    @Message("账号不存在")
    public static final int ACCOUNT_NOT_EXIST = 101;
    @Message("账号被禁用")
    public static final int ACCOUNT_BANED = 102;
    @Message("密码不正确")
    public static final int PASSWORD_INCORRECT = 103;
    @Message("输入格式错误")
    public static final int FORMAT_INCORRECT = 104;
    @Message("账号在其他地方登陆,IP:{0}")
    public static final int ACCOUNT_OTHER_LOGIN = 105;
    @Message("账号未登陆")
    public static final int ACCOUNT_NOT_LOGIN = 106;
    @Message("账号未登陆或登陆超时")
    public static final int ACCOUNT_NOT_LOGIN_OR_SESSION_TIMEOUT = 107;
    @Message("令牌过期请重新获取")
    public static final int TOKEN_TIMEOUT = 108;

    @Message("请输入数字")
    public static final int INPUT_NUMBER = 200;

    @Message("权限不足[{0}] [{1}] 验证 [{2}] 失败")
    public static final int LACK_OF_PERMISSION_RESOURCE_INCORRECT = 402;
    @Message("权限不足[{0}]")
    public static final int LACK_OF_PERMISSION = 403;
    @Message("目标不存在[{0}]")
    public static final int TARGET_NOT_EXIST = 404;


    protected Logger logger;

    public Result() {
        logger = LoggerFactory.getLogger(getClass());
    }


    @PostConstruct
    public void report() {
        ResultHelper.results.add(this);
    }



}
