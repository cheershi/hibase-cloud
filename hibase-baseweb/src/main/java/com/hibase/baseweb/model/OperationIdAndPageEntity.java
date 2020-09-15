package com.hibase.baseweb.model;


import com.hibase.baseweb.annotation.HibaseValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author hufeng
 * @date 2019/05/16
 */
@Data
@ApiModel("操作对象id和分页")
@HibaseValid
public class OperationIdAndPageEntity extends PageBase {

    @ApiModelProperty(value = "id", example = "1232342")
    @NotBlank(message = "请选择记录")
    private String id;
}
