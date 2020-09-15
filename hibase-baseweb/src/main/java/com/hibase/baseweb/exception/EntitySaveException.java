package com.hibase.baseweb.exception;

import com.hibase.baseweb.constant.ExceptionCode;

/**
 * 数据保存异常
 *
 * @author hufeng
 * @date 2019/03/28
 */
public class EntitySaveException extends HibaseException {

    public EntitySaveException() {
    }

    public EntitySaveException(int code, String message) {

        super(code, message);
    }

    public EntitySaveException(String message) {

        super(ExceptionCode.ENTITY_SAVE_EXCEPTION.getCode(), message);
    }

    public EntitySaveException(int code , String message, Throwable cause) {
        super(code, message, cause);
    }

    public EntitySaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
