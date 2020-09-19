package com.hibase.baseweb.core.exception.web;

import com.hibase.baseweb.constant.exception.ExceptionCode;
import com.hibase.baseweb.core.exception.HibaseException;

/**
 * 类备注：vo校验
 *
 * @author chenfeng
 * @version 1.0
 * @date 2019-03-27 21:10
 * @desc
 * @since 1.8
 */

public class VoValidException extends HibaseException {

    public VoValidException() {
    }

    public VoValidException(int code, String message) {
        super(code, message);
    }

    public VoValidException(int code , String message, Throwable cause) {
        super(code, message, cause);
    }

    public VoValidException(String message) {
        super(ExceptionCode.VO_VALID_EXCEPTION.getCode(), message);
    }

    public VoValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
