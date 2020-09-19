package com.hibase.baseweb.constant.mybatis;

/**
 * 数据权限过滤类型
 */
public enum DataPermType {

    ENTITY_SAVE_EXCEPTION(1, "实体保存异常"),
    ENTITY_UNIQUEKEY_EXCEPTION(2, "唯一键重复异常"),
    ENTITY_VALID_EXCEPTION(3, "实体类校验异常"),
    SERVICE_BUSINESS_EXCEPTION(4, "业务类异常");

    private int code;

    private String message;

    DataPermType(int code, String message) {
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
