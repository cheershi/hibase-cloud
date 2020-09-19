package com.hibase.baseweb.core.exception;

import com.hibase.baseweb.constant.exception.ExceptionCode;

/**
 * 自动生成id异常
 *
 * @author chenfeng
 * @date 2019/01/18
 */
public class GenerateIdException extends HibaseException {

    public GenerateIdException(int code, String message) {
        super(code, message);
    }

    public GenerateIdException() {
    }

    public GenerateIdException(int code , String message, Throwable cause) {
        super(code, message, cause);
    }

    public GenerateIdException(String message) {
        super(ExceptionCode.GENERATE_ID_EXCEPTION.getCode(), message);
    }

    public GenerateIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
