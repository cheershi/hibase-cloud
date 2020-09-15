package com.hibase.baseweb.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 唯一key注解
 *
 * @author hufeng
 * @date 2019/03/28
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
@Documented
public @interface UniqueKey {

    /**
     * 列名
     */
    String group() default "default";

    @AliasFor("group")
    String value() default "default";

    /**
     * 用于提示
     */
    String message() default "";
}
