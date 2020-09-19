package com.hibase.baseweb.utils;

import cn.hutool.core.util.StrUtil;
import com.hibase.baseweb.core.exception.HibaseException;


/**
 * bibase异常工具类
 *
 * @author chenfeng
 * @date 2019/01/18
 */
public class HibaseExceptionUtils {

    /**
     * 返回一个新的异常，统一构建，方便统一处理
     *
     * @param msg 消息
     * @param t   异常信息
     * @return 返回异常
     */
    public static HibaseException hbException(String msg, Throwable t, Object... params) {
        return new HibaseException(StrUtil.format(msg, params), t);
    }

    /**
     * 重载的方法
     *
     * @param msg 消息
     * @return 返回异常
     */
    public static HibaseException hbException(String msg, Object... params) {
        return new HibaseException(StrUtil.format(msg, params));
    }

    /**
     * 重载的方法
     *
     * @param t 异常
     * @return 返回异常
     */
    public static HibaseException hbException(Throwable t) {
        return new HibaseException(t);
    }

    /**
     * 组建message信息
     */
    public static String buildMessage(String message, Throwable cause) {

        if (cause == null) {

            return message;
        }
        StringBuilder sb = new StringBuilder(64);
        if (message != null) {
            sb.append(message).append("; ");
        }
        sb.append("hibase exception is ").append(cause);
        return sb.toString();
    }

    /**
     * 获取父级的异常原因
     */
    public static Throwable getRootCause(Throwable original) {
        if (original == null) {
            return null;
        }
        Throwable rootCause = null;
        Throwable cause = original.getCause();
        while (cause != null && cause != rootCause) {
            rootCause = cause;
            cause = cause.getCause();
        }
        return rootCause;
    }

    /**
     * 获取父级的异常原因，如果没有则返回自身的
     */
    public static Throwable getMostSpecificCause(Throwable original) {
        Throwable rootCause = getRootCause(original);
        return (rootCause != null ? rootCause : original);
    }
}
