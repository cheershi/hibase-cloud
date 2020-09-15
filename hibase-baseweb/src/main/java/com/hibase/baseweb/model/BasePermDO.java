package com.hibase.baseweb.model;

import lombok.Data;

/**
 * 数据权限DO
 *
 * @author hufeng
 * @date 2019/05/20
 */
@Data
public class BasePermDO extends BaseDO {

    /**
     * 所属平台id
     */
    private String platformId;

    /**
     * 所属组织id
     */
    private String orgId;
}
