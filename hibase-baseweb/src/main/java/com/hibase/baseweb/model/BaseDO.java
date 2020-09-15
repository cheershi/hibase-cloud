package com.hibase.baseweb.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.hibase.baseweb.constant.GlobalConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * mybatis的父级do
 *
 * @author hufeng
 * @date 2019/01/10
 */
@Data
public class BaseDO implements Serializable {

    /**
     * 主键id
     */
    //@ApiModelProperty(value = "主键id（新增时不需要带id，修改时需要）", example = "1111")
    @ApiModelProperty(hidden = true)
    private String id;

    /**
     * 创建人账号
     */
    @ApiModelProperty(hidden = true)
    private String createBy;

    /**
     * 创建人时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改人账号
     */
    @ApiModelProperty(hidden = true)
    private String updateBy;

    /**
     * 修改人时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 是否已删除
     */
    @ApiModelProperty(hidden = true)
    @TableField(exist = true)
    @TableLogic(value = GlobalConstant.BE_DELETE_NO + "", delval = GlobalConstant.BE_DELETE_YES + "")
    private Boolean beDeleted;
}
