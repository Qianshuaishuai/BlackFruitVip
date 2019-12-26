package com.heiguo.blackfruitvip;

public class Constant {
    public static final Boolean DEBUG = true;

    //网络请求状态码
    public static final int REQUEST_SUCCESS = 10000;
    public static final int REQUEST_ERROR = 10002;

    //验证码类型相关
    public static final int CODE_REGISTER = 101;
    public static final int CODE_CHANGE_PASSWORD = 102;
    public static final int CODE_GET_ADDRESS = 201;

    //Api请求相关数据
    public static final String BASE_URL = "http://010.ming123.net";
    public static final String URL_CODE = "/v1/user/code";
    public static final String URL_AUTO = "/v1/user/auto";
    public static final String URL_STORE_LIST = "/v1/store/list";
    public static final String URL_GET = "/v1/user/get";
    public static final String URL_LOGOUT = "/v1/user/logout";
    public static final String URL_REGISTER = "/v1/user/register";
    public static final String URL_LOGIN = "/v1/user/login";
    public static final String URL_FORGET = "/v1/user/forget";
    public static final String URL_MAIN_ALL = "/v1/home/all";
}
