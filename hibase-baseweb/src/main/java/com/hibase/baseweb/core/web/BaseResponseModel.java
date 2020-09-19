package com.hibase.baseweb.core.web;

import lombok.Data;

/**
 * 父类响应类
 *
 * @author chenfeng
 * @date 2019/03/28
 */
@Data
public class BaseResponseModel {

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回信息
     */
    private String message;

    public BaseResponseModel(String message) {

        this.message = message;
    }

    public BaseResponseModel(int code) {

        this.code = code;
    }

    public BaseResponseModel(int code, String message) {
        
        this.code = code;
        this.message = message;
    }
}
