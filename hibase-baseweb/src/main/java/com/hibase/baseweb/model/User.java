package com.hibase.baseweb.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息业务层对象
 *
 * @author hufeng
 * @create 2018-08-30 20:09
 */
@Data
public class User implements Serializable {

    /**
     * 用户id
     */
    private String id;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 真实姓名
     */
    private String sysName;

    /**
     * 组织id
     */
    private String orgId;

    /**
     * 组织id
     */
    private String orgFullId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 所属平台id（顶级组织id）
     */
    private String platformId;

    /**
     * 允许访问的数据类型（）
     */
    private List<String> dataPerm;

    /**
     * 司机id
     */
    private String driverId;

    /**
     * 所属公司
     */
    private String mdsCompanyId;
}
