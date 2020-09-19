package com.hibase.baseweb.annotation.exception;

import java.lang.annotation.*;

/**
 * hibase异常拦截注解
 *
 * @author chenfeng
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HibaseAspectException {
}
