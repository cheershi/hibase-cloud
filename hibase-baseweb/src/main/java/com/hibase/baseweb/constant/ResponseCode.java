package com.hibase.baseweb.constant;

/**
 * 基础项目错误码统一定义
 */
public enum ResponseCode {

    /*****************服务器返回状态码*****************/
    SUCCESS(0, "成功"),
    SERVER_ERROR(999, "服务器异常"),

    /*****************登录返回状态码*****************/
    USER_NOTLOGIN(1000, "未登录"),
    ADMIN_USER_NOT_EXISTS(1001, "用户名不存在"),
    ADMIN_USER_DISABLED(1004, "用户已被禁用"),
    ADMIN_USER_PASSWORD_ERROR(1002, "密码错误"),
    ADMIN_LOGIN_REPEAT_ERROR(1003, "请勿重复登录"),

    /*****************业务校验返回状态码*****************/
    MISS_INFO(1, "缺少必要信息"),
    INVALID_PARAM(2, "参数错误"),
    DATA_REPEAT(2, "数据重复"),
    DATA_NOT_EXISTS(3, "数据不存在");

    private int code;

    private String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
