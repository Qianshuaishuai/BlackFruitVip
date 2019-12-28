package com.heiguo.blackfruitvip;

public class Constant {
    public static final Boolean DEBUG = true;

    //联系人号码
    public static final String CONNECTION_PHONE = "17090092828";

    //网络请求状态码
    public static final int REQUEST_SUCCESS = 10000;
    public static final int REQUEST_ERROR = 10002;

    //Activity回传码
    public static final int GOOD_DETAIL_BACK_REQUEST_CODE = 111;

    //地址编辑模式
    public static final int ADDRESS_MODE_GO_ADD = 1111;
    public static final int ADDRESS_MODE_GO_EDIT = 1112;
    //地址选择模式
    public static final int CITY_MODE_FROM_ADDRESS = 1113;
    public static final int CITY_MODE_FROM_MAIN = 1114;
    //地址列表模式
    public static final int ADDRESS_LIST_MODE_NORMAL = 1115;
    public static final int ADDRESS_LIST_MODE_PICKER = 1116;

    //验证码类型相关
    public static final int CODE_REGISTER = 101;
    public static final int CODE_CHANGE_PASSWORD = 102;
    public static final int CODE_GET_ADDRESS = 201;

    //Api请求相关数据
    public static final String BASE_URL = "http://010.ming123.net";
    public static final String URL_CODE = "/v1/user/code";
    public static final String URL_AUTO = "/v1/user/auto";
    public static final String URL_GET = "/v1/user/get";
    public static final String URL_LOGOUT = "/v1/user/logout";
    public static final String URL_REGISTER = "/v1/user/register";
    public static final String URL_LOGIN = "/v1/user/login";
    public static final String URL_FORGET = "/v1/user/forget";
    public static final String URL_BUY_VIP = "/v1/user/vip";
    public static final String URL_MAIN_ALL = "/v1/home/all";
    public static final String URL_STORE_LIST = "/v1/store/list";
    public static final String URL_GOOD_LIST = "/v1/good/list";
    public static final String URL_ADDRESS_LIST = "/v1/address/list";
    public static final String URL_ADDRESS_EDIT = "/v1/address/edit";
}
