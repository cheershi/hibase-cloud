package com.hibase.baseweb.model;


import com.hibase.baseweb.annotation.HibaseValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hufeng
 * @date 2019/05/16
 */
@Data
@ApiModel("操作对象id和分页，ID可为空")
@HibaseValid
public class OperationIdNullAndPageEntity extends PageBase {

    @ApiModelProperty(value = "id", example = "1232342")
    private String id;
}
