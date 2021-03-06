package com.hibase.baseweb.core.exception;

import com.hibase.baseweb.constant.exception.ExceptionCode;

/**
 * service服务异常
 *
 * @author chenfeng
 * @date 2019/03/28
 */
public class BusinessException extends HibaseException {

    public BusinessException() {
    }

    public BusinessException(int code, String message) {
        super(code, message);
    }

    public BusinessException(int code , String message, Throwable cause) {
        super(code, message, cause);
    }

    public BusinessException(String message) {
        super(ExceptionCode.SERVICE_BUSINESS_EXCEPTION.getCode(), message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
