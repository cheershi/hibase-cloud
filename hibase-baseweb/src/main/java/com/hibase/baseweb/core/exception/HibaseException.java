package com.hibase.baseweb.core.exception;

import com.hibase.baseweb.constant.exception.ExceptionCode;
import com.hibase.baseweb.utils.HibaseExceptionUtils;
import lombok.Data;

/**
 * hibase顶级异常类
 *
 * @author chenfeng
 * @date 2019/01/18
 */
@Data
public class HibaseException extends RuntimeException {

    /**
     * 状态码
     */
    protected int code = ExceptionCode.DEFAULT_EXCEPTION.getCode();

    public HibaseException() {
    }

    public HibaseException(String message) {
        super(message);
    }

    public HibaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public HibaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HibaseException(int code , String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public HibaseException(Throwable cause) {
        super(cause);
    }

    public HibaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return HibaseExceptionUtils.buildMessage(super.getMessage(), getCause());
    }


    /**
     * 获取父级的异常原因
     */
    public Throwable getRootCause() {
        return HibaseExceptionUtils.getRootCause(this);
    }

    /**
     * 获取父级的异常原因，如果没有则返回自身的
     */
    public Throwable getMostSpecificCause() {

        return HibaseExceptionUtils.getMostSpecificCause(this);
    }


}
