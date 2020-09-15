package com.hibase.baseweb.web;


import com.hibase.baseweb.annotation.HibasePostRequestMapping;
import com.hibase.baseweb.generate.GenerateCode;
import com.hibase.baseweb.generate.GenerateProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自动生成代码
 *
 * @author hufeng
 * @date 2019/03/21
 */
@Api(tags = "自动生成代码")
@RestController
@RequestMapping("generate")
@Slf4j
public class GenerateController extends BaseController {

    @ApiOperation(value = "自动生成代码", httpMethod = "POST")
    @HibasePostRequestMapping(value = "generateCode")
    public ResponseModel generateCode(@RequestBody GenerateProperties properties) {

        GenerateCode.generateCode(properties);

        return super.successMsg();
    }
}
