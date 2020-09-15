package com.hibase.baseweb.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 字典 值填充接口
 *
 * @author hufeng
 * @date 2019/04/08
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
@Documented
public @interface RelevanceFill {

    /**
     * 指定获取哪个serviceBean
     */
    Class serviceClass();

    /**
     * 关联的字段名称
     */
    String relevanceKey() default "";

    /**
     * 需要填充的字段名称
     */
    String fillProperties();

    /**
     * 需要填充的字段名称
     */
    Class resultClass();

    @AliasFor("columnName")
    String value() default "default";

    /**
     * 为空是否抛异常
     */
    boolean throwException() default false;
}
