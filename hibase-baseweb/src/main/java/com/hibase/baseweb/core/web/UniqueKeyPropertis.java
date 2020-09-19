package com.hibase.baseweb.core.web;

import lombok.Data;

/**
 * 唯一键属性对象
 *
 * @author chenfeng
 * @date 2019/03/30
 */
@Data
public class UniqueKeyPropertis {

    /**
     * 列属性值
     */
    private String value;

    /**
     * 提示
     */
    private String message;
}