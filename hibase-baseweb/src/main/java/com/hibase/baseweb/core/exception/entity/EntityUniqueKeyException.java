package com.hibase.baseweb.core.exception.entity;

import com.hibase.baseweb.constant.exception.ExceptionCode;
import com.hibase.baseweb.core.exception.HibaseException;

/**
 * 类备注：entity唯一key校验
 *
 * @author chenfeng
 * @version 1.0
 * @date 2019-03-27 21:10
 * @desc
 * @since 1.8
 */

public class EntityUniqueKeyException extends HibaseException {

    public EntityUniqueKeyException() {
    }

    public EntityUniqueKeyException(int code, String message) {

        super(code, message);
    }

    public EntityUniqueKeyException(String message) {
        super(ExceptionCode.ENTITY_UNIQUEKEY_EXCEPTION.getCode(), message);
    }

    public EntityUniqueKeyException(int code , String message, Throwable cause) {
        super(code, message, cause);
    }

    public EntityUniqueKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
