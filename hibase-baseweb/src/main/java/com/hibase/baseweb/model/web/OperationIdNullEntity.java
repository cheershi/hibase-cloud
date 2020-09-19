package com.hibase.baseweb.model.web;


import com.hibase.baseweb.annotation.valid.HibaseValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenfeng
 * @date 2019-4-17
 */
@Data
@ApiModel("操作对象id，id可为空")
@HibaseValid
public class OperationIdNullEntity {

    @ApiModelProperty(value = "id", example = "1232342")
    private String id;
}
