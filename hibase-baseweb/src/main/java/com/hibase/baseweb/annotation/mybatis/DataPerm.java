package com.hibase.baseweb.annotation.mybatis;

import java.lang.annotation.*;


/**
 * 数据权限过滤
 * @author chenfeng
 * @date 2019/5/26
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPerm {

    /**
     * TODO 数据权限过滤sql，优先级高于配置
     * @return
     */
    String value() default "";

    /**
     * 是否开启数据权限
     * @return
     */
    boolean enable() default true;
}
