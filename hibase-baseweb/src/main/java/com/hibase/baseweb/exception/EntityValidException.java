package com.hibase.baseweb.exception;

import com.hibase.baseweb.constant.ExceptionCode;

/**
 * 类备注：entity校验
 *
 * @author hufeng
 * @version 1.0
 * @date 2019-03-27 21:10
 * @desc
 * @since 1.8
 */

public class EntityValidException extends HibaseException {

    public EntityValidException() {
    }

    public EntityValidException(int code, String message) {
        super(code, message);
    }

    public EntityValidException(int code , String message, Throwable cause) {
        super(code, message, cause);
    }

    public EntityValidException(String message) {
        super(ExceptionCode.ENTITY_VALID_EXCEPTION.getCode(), message);
    }

    public EntityValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
