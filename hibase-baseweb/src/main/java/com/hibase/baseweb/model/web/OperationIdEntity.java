package com.hibase.baseweb.model.web;


import com.hibase.baseweb.annotation.valid.HibaseValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author chenfeng
 * @date 2019-4-17
 */
@Data
@ApiModel("操作对象id")
@HibaseValid
public class OperationIdEntity {

    @ApiModelProperty(value = "id", example = "1232342")
    @NotBlank(message = "请选择记录")
    private String id;
}
