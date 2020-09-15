package com.hibase.baseweb.web;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.hibase.baseweb.annotation.HibasePostRequestMapping;
import com.hibase.baseweb.constant.ResponseCode;
import com.hibase.baseweb.model.BaseDO;
import com.hibase.baseweb.model.PageBase;
import com.hibase.baseweb.service.BaseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 自动curd controller
 *
 * @author hufeng
 * @date 2019/03/12
 */
@RestController
@RequestMapping("comm")
@Slf4j
public class CommController extends BaseController {

    @ApiOperation(value = "通用查询", httpMethod = "POST")
    @HibasePostRequestMapping(value = "selectList")
    public ResponseModel selectList(@RequestBody PageBase pageBase, HttpServletRequest request) {

        // 拿到请求头信息
        String entityClass = super.getEntityClass(request);
        if (StrUtil.isBlank(entityClass)) {

            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(),"实体类不存在");
        }
        try {
            BaseService iService = getEntityMapper(entityClass);

            Page page = new Page(pageBase.getCurrentPage(), pageBase.getPageSize());

            IPage pageInfo = iService.page(page, new QueryWrapper());

            return super.successMsg(pageInfo);

        } catch (Exception e) {

            log.error("实体类不存在", e);
            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "实体类不存在");
        }
    }

    @ApiOperation(value = "根据id通用查询一条记录", httpMethod = "POST")
    @HibasePostRequestMapping(value = "selectOne")
    public ResponseModel selectOne(String id, HttpServletRequest request) {

        // 拿到请求头信息
        String entityClass = super.getEntityClass(request);

        if (StrUtil.isBlank(id)) {

            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "id不能为空");
        }

        if (StrUtil.isBlank(entityClass)) {

            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "实体类不存在");
        }
        try {

            BaseService iService = getEntityMapper(entityClass);

            return super.successMsg("object", iService.getById(id));

        } catch (Exception e) {

            log.error("实体类不存在", e);
            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "实体类不存在");
        }
    }

    @ApiOperation(value = "通用新增一条记录", httpMethod = "POST")
    @HibasePostRequestMapping(value = "insert")
    public ResponseModel insert(@RequestBody Map<String, Object> map, HttpServletRequest request) {

        // 拿到请求头信息
        String entityClass = super.getEntityClass(request);

        if (StrUtil.isBlank(entityClass)) {

            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "实体类不存在");
        }

        try {

            Class entity = Class.forName(entityClass);
            BaseService iService = getEntityMapper(entityClass);

            BaseDO baseDO = (BaseDO) new Gson().fromJson(map.toString(), entity);

            iService.save(baseDO);

            return super.successMsg();

        } catch (ClassNotFoundException e) {

            log.error("实体类不存在", e);
            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "实体类不存在");
        } catch (Exception e) {

            log.error("操作失败", e);
            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "操作失败");
        }
    }

    @ApiOperation(value = "通用修改一条记录", httpMethod = "POST")
    @HibasePostRequestMapping(value = "update")
    public ResponseModel update(@RequestBody Map<String, Object> map, HttpServletRequest request) {

        // 拿到请求头信息
        String entityClass = super.getEntityClass(request);

        if (StrUtil.isBlank(entityClass)) {

            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "实体类不存在");
        }

        try {

            Class entity = Class.forName(entityClass);
            BaseService iService = getEntityMapper(entityClass);

            BaseDO baseDO = (BaseDO)new Gson().fromJson(map.toString(), entity);

            iService.updateById(baseDO);

            return super.successMsg();

        } catch (ClassNotFoundException e) {

            log.error("实体类不存在", e);
            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "实体类不存在");
        } catch (Exception e) {

            log.error("操作失败", e);
            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "操作失败");
        }
    }

    @ApiOperation(value = "根据id通用删除一条记录", httpMethod = "POST")
    @HibasePostRequestMapping(value = "delete")
    public ResponseModel delete(String id, HttpServletRequest request) {

        // 拿到请求头信息
        String entityClass = super.getEntityClass(request);

        if (StrUtil.isBlank(id)) {

            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "id不能为空");
        }

        if (StrUtil.isBlank(entityClass)) {

            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "实体类不存在");
        }
        try {

            BaseService iService = getEntityMapper(entityClass);

            Class entity = Class.forName(entityClass);

            BaseDO object = (BaseDO) entity.newInstance();

            object.setId(id);

            iService.deleteBy(object);

            return super.successMsg();

        } catch (Exception e) {

            log.error("实体类不存在", e);
            return super.failMsg(ResponseCode.DATA_NOT_EXISTS.getCode(), "实体类不存在");
        }
    }
}
