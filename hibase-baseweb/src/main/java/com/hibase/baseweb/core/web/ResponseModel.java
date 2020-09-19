package com.hibase.baseweb.core.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hibase.baseweb.constant.web.ResponseCode;
import lombok.Data;

/**
 * WEB响应类
 *
 * @author chenfeng
 * @create 2017-12-13
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> extends BaseResponseModel {

    /**
     * 数据
     */
    private T data;

    ResponseModel() {
        super(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    ResponseModel(T data) {
        super(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
        this.data = data;
    }

    ResponseModel(int code, String message) {
        super(code, message);
    }

    ResponseModel(T data, int code, String message) {
        super(code, message);
        this.data = data;
    }
}
