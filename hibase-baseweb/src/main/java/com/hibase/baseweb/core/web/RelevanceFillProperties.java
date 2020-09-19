package com.hibase.baseweb.core.web;

import lombok.Data;

import java.util.Set;

/**
 * 注解填充对象
 *
 * @author chenfeng
 * @date 2019/04/08
 */
@Data
public class RelevanceFillProperties {

    /**
     * 指定获取哪个serviceBean
     */
    private Class serviceClass;

    /**
     * 关联的字段名称
     */
    private String relevanceKey;

    /**
     * 指定返回的类型
     */
    private String fillProperties;

    /**
     * 返回的class类型
     */
    private Class resultClass;

    /**
     * 为空是否抛异常
     */
    private boolean throwException;

    /**
     * 属性名称
     */
    private String propertiesName;

    /**
     * 需要查询传入的值
     */
    private Set<Object> searchValue;

    /**
     * 从数据库查询出的值
     */
    private Object resultValue;
}
