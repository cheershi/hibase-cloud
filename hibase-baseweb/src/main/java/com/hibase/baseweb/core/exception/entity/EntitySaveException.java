package com.hibase.baseweb.core.exception.entity;

import com.hibase.baseweb.constant.exception.ExceptionCode;
import com.hibase.baseweb.core.exception.HibaseException;

/**
 * 数据保存异常
 *
 * @author chenfeng
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
