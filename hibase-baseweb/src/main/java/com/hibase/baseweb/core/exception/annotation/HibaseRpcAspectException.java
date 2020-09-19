package com.hibase.baseweb.core.exception.annotation;

import java.lang.annotation.*;

/**
 * hibaseRPC异常拦截注解
 *
 * @author chenfeng
 * @date 2019/03/29
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HibaseRpcAspectException {
}
