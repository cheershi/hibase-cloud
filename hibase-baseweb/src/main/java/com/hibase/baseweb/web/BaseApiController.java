package com.hibase.baseweb.web;

import com.hibase.baseweb.constant.ResponseCode;

/**
 * <p>
 * api base控制器
 * </p>
 *
 * @author hufeng
 * @since 2019-05-15
 */
public abstract class BaseApiController<T> {

    /**
     * 返回成功
     *
     * @return
     */
    public ApiResponseModel<T> successMsg(String messageId) {

        return new ApiResponseModel<T>(messageId);
    }

    /**
     * 返回成功，自定义状态码
     *
     * @return
     */
    public ApiResponseModel<T> successMsg(String msg, T data, String messageId) {

        return new ApiResponseModel<T>(data, ResponseCode.SUCCESS.getCode(), messageId, msg);
    }

    /**
     * 返回成功，自定义状态码
     *
     * @return
     */
    public ApiResponseModel<T> successMsg(String messageId, T data) {

        return new ApiResponseModel<T>(messageId, data);
    }

    /**
     * 返回成功，自定义状态码
     *
     * @return
     */
    public ApiResponseModel<T> failMsg(int code, String msg, String messageId) {

        return new ApiResponseModel<T>(code, msg, messageId);
    }

    /**
     * 返回成功，自定义状态码
     *
     * @return
     */
    public ApiResponseModel<T> failMsg(int code, String msg, String messageId, T data) {

        return new ApiResponseModel<T>(data, code, messageId, msg);
    }
}
