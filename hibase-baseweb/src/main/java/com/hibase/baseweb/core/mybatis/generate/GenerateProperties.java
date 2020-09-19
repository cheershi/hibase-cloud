package com.hibase.baseweb.core.mybatis.generate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 自动生成代码封装属性
 *
 * @author chenfeng
 * @date 2019/03/21
 */
@ApiModel("自动生成代码vo")
@Data
public class GenerateProperties {

    @ApiModelProperty(value = "类名的创建人，默认为admin", example = "admin")
    private String author = "admin";

    @ApiModelProperty(value = "生成的日期类（java.sql.date 或者 java.util.Date 或者 java.time.LocalTime），默认为java.util.Date", example = "java.util.Date")
    private String dateType = "java.util.date";

    @ApiModelProperty(value = "数据源url", example = "jdbc:mysql://192.168.1.125:3306/vms?useUnicode=true&useSSL=false&characterEncoding=utf8")
    private String dataSourceUrl;

    @ApiModelProperty(value = "驱动名称", example = "com.mysql.jdbc.Driver")
    private String driverName;

    @ApiModelProperty(value = "用户名", example = "root")
    private String username;

    @ApiModelProperty(value = "密码", example = "123")
    private String password;

    @ApiModelProperty(value = "父级包名称", example = "com.hichain.vms")
    private String parentPackName;

    @ApiModelProperty(value = "模块名称", example = "vehicle")
    private String moduleName;

    @ApiModelProperty(value = "自动生成代码的表名", example = "vms_vehicle")
    private String tableName;
}
