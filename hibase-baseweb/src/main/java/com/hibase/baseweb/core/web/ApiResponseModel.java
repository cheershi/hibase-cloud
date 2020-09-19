package com.hibase.baseweb.core.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hibase.baseweb.constant.web.ResponseCode;
import lombok.Data;

import java.util.Date;

/**
 * API响应类
 *
 * @author chenfeng
 * @create 2017-12-13
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseModel<T> extends ResponseModel {

    /**
     * messageId
     */
    private String messageId;

    /**
     * 响应时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date responseTime;

    ApiResponseModel(String messageId) {

        super(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
        this.messageId = messageId;
    }

    ApiResponseModel(String messageId, T data) {
        super(data, ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
        this.messageId = messageId;
    }

    ApiResponseModel(int code, String message, String messageId) {
        super(code, message);
        this.messageId = messageId;
    }

    ApiResponseModel(T data, int code, String messageId, String message) {
        super(data, code, message);
        this.messageId = messageId;
    }
}
