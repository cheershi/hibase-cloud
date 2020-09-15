package com.hibase.baseweb.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页对象
 *
 * @author hufeng
 * @create 2018-08-30 20:11
 */
@Data
public class PageBase {

    @ApiModelProperty(value = "当前页数", example = "1", notes = "默认为1")
    private Integer currentPage = 1;

    @ApiModelProperty(value = "页大小", example = "10", notes = "默认为10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "关键字模糊搜索", example = "123", notes = "关键字模糊搜索")
    private String keyWord;
}
