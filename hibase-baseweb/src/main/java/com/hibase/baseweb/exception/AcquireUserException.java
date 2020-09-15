package com.hibase.baseweb.exception;

import com.hibase.baseweb.constant.ExceptionCode;

/**
 * 获取用户信息异常
 *
 * @author hufeng
 * @date 2019/01/18
 */
public class AcquireUserException extends HibaseException {

    public AcquireUserException() {
    }

    public AcquireUserException(int code, String message) {
        super(code, message);
    }

    public AcquireUserException(String message) {
        super(ExceptionCode.ACQUIRE_USER_EXCEPTION.getCode(), message);
    }

    public AcquireUserException(int code , String message, Throwable cause) {
        super(code, message, cause);
    }

    public AcquireUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
