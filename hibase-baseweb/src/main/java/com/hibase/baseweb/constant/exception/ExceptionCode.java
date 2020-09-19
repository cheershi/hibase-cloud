package com.hibase.baseweb.constant.exception;

/**
 * 异常状态码定义
 */
public enum ExceptionCode {

    ENTITY_SAVE_EXCEPTION(1, "实体保存异常"),
    ENTITY_UNIQUEKEY_EXCEPTION(2, "唯一键重复异常"),
    ENTITY_VALID_EXCEPTION(3, "实体类校验异常"),
    SERVICE_BUSINESS_EXCEPTION(4, "业务类异常"),
    ACQUIRE_USER_EXCEPTION(5, "用户信息获取异常"),
    GENERATE_ID_EXCEPTION(6, "生成id获取异常"),
    VO_VALID_EXCEPTION(7, "vo校验类异常"),
    EXCEL_BUSINESS_EXCEPTION(8, "表格业务校验类异常"),
    DATA_PERM_EXCEPTION(9, "业务类异常"),
    DEFAULT_EXCEPTION(1000, "hibase异常");

    private int code;

    private String message;

    ExceptionCode(int code, String message) {
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
