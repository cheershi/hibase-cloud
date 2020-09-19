package com.hibase.baseweb.core.exception.dataperm;

import com.hibase.baseweb.constant.exception.ExceptionCode;
import com.hibase.baseweb.core.exception.HibaseException;

/**
 * 数据权限异常
 *
 * @author chenfeng
 * @date 2019/03/28
 */
public class DataPermException extends HibaseException {

    public DataPermException() {
    }

    public DataPermException(int code, String message) {
        super(code, message);
    }

    public DataPermException(int code , String message, Throwable cause) {
        super(code, message, cause);
    }

    public DataPermException(String message) {
        super(ExceptionCode.DATA_PERM_EXCEPTION.getCode(), message);
    }

    public DataPermException(String message, Throwable cause) {
        super(message, cause);
    }
}
