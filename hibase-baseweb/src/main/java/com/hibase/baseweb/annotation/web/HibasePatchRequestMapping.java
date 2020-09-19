package com.hibase.baseweb.annotation.web;

import com.hibase.baseweb.annotation.exception.HibaseAspectException;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * hibase控制类注解，包装restcontroller
 * @author 陈丰
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.PATCH)
@HibaseAspectException
public @interface HibasePatchRequestMapping {

    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] value() default {};

    @AliasFor(annotation = RequestMapping.class, attribute = "name")
    String name() default "";

    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] path() default {};

    @AliasFor(annotation = RequestMapping.class, attribute = "params")
    String[] params() default {};

    @AliasFor(annotation = RequestMapping.class, attribute = "headers")
    String[] headers() default {};

    @AliasFor(annotation = RequestMapping.class, attribute = "consumes")
    String[] consumes() default {};

    @AliasFor(annotation = RequestMapping.class, attribute = "produces")
    String[] produces() default {};
}
