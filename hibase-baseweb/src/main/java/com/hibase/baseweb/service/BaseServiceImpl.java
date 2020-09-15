package com.hibase.baseweb.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hibase.baseweb.annotation.RelevanceFill;
import com.hibase.baseweb.annotation.UniqueKey;
import com.hibase.baseweb.config.help.GenerateIdHelper;
import com.hibase.baseweb.constant.GlobalConstant;
import com.hibase.baseweb.exception.BusinessException;
import com.hibase.baseweb.exception.EntitySaveException;
import com.hibase.baseweb.exception.EntityUniqueKeyException;
import com.hibase.baseweb.exception.EntityValidException;
import com.hibase.baseweb.model.BaseDO;
import com.hibase.baseweb.model.BasePermDO;
import com.hibase.baseweb.model.PageBase;
import com.hibase.baseweb.model.User;
import com.hibase.baseweb.utils.*;
import com.hibase.baseweb.valid.ValidationResult;
import com.hibase.baseweb.web.RelevanceFillProperties;
import com.hibase.baseweb.web.UniqueKeyPropertis;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * base service
 *
 * @author hufeng
 * @date 2019/01/15
 */
@Slf4j
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseDO> implements BaseService<T> {

    @Autowired
    protected M baseMapper;

    @Override
    public M getBaseMapper() {
        return baseMapper;
    }

    /**
     * 判断数据库操作是否成功
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    protected boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    protected Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }

    /**
     * 批量操作 SqlSession
     */
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(currentModelClass());
    }

    /**
     * 释放sqlSession
     *
     * @param sqlSession session
     */
    protected void closeSqlSession(SqlSession sqlSession) {
        SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(currentModelClass()));
    }

    /**
     * 获取 SqlStatement
     *
     * @param sqlMethod ignore
     * @return ignore
     */
    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }

    @Override
    public boolean save(T entity) {

        this.settingInfo(entity);

        return retBool(baseMapper.insert(entity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOnlyById(T entity) {

        return retBool(baseMapper.updateById(entity));
    }

    /**
     * 通用保存
     */
    @Override
    public T store(T entity) {

        ValidationResult result = ValidationUtils.validateEntity(entity);

        if (result.hasErrors()) {

            throw new EntityValidException(result.getDefaultMessage());
        }

        this.saveOrUpdate(entity);

        return entity;
    }

    /**
     * 通用批量保存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean storeBatch(Collection<T> entityList, int batchSize) {

        return this.saveOrUpdateBatch(entityList, batchSize);
    }

    /**
     * 通用批量保存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean storeBatch(Collection<T> entityList) {

        return this.saveOrUpdateBatch(entityList);
    }

    @Override
    public boolean countById(Serializable id) {

        return retBool(this.count(new QueryWrapper<T>().eq("id", id)));
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (T anEntityList : entityList) {
                this.settingInfo(anEntityList);
                batchSqlSession.insert(sqlStatement, anEntityList);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    /**
     * 批量更新
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatch(Collection<T> entityList, int batchSize) {
        String sqlStatement = sqlStatement(SqlMethod.UPDATE_BY_ID);
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (T anEntityList : entityList) {

                this.settingInfo(anEntityList);

                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, anEntityList);

                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    /**
     * 批量更新
     *
     * @param entityList ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatch(Collection<T> entityList) {

        return this.updateBatch(entityList, 1000);
    }


    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdate(T entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
            String keyProperty = tableInfo.getKeyProperty();
            Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
            Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());

            // 处理唯一key处理
            checkUniquekey(entity, tableInfo);

            settingInfo(entity);

            if (StringUtils.checkValNull(idVal)) {

                return save(entity);
            } else {

                Assert.notNull(getById((Serializable) idVal), "错误：因为修改的记录不存在!");
                return updateById(entity);
            }
        }
        return false;
    }

    /**
     * 处理唯一key处理
     */
    public void checkUniquekey(T entity, TableInfo tableInfo) {

        List<Field> fieldList = ReflectionKit.getFieldList(ClassUtils.getUserClass(entity));

        if (MyUtils.listIsNotNull(fieldList)) {

            Map<String, Map<String, UniqueKeyPropertis>> temp = new HashMap<>();

            for (Field field : fieldList) {

                Map<String, UniqueKeyPropertis> column = new HashMap<>();

                UniqueKey uniqueKey = field.getAnnotation(UniqueKey.class);
                if (uniqueKey != null) {

                    try {
                        // TODO 之后调整为java properties属性查询
                        TableFieldInfo info = TablePropertiesHelper.getTableFieldInfo(field.getName(), tableInfo);

                        UniqueKeyPropertis propertis = new UniqueKeyPropertis();
                        propertis.setMessage(uniqueKey.message());

                        if (!temp.containsKey(uniqueKey.group())) {

                            Object value = ReflectionKit.getMethodValue(entity, info.getProperty());

                            if (value == null || StrUtil.isBlank(value.toString())) {

                                throw new EntityUniqueKeyException("处理唯一key失败，" + info.getProperty() + "不能为空");
                            }

                            propertis.setValue(value.toString());

                            column.put(info.getColumn(), propertis);
                            temp.put(uniqueKey.group(), column);
                        } else {

                            Object value = ReflectionKit.getMethodValue(entity, info.getProperty()).toString();

                            if (value == null || StrUtil.isBlank(value.toString())) {

                                throw new EntityUniqueKeyException("处理唯一key失败，" + info.getProperty() + "不能为空");
                            }

                            propertis.setValue(value.toString());

                            temp.get(uniqueKey.group()).put(info.getColumn(), propertis);
                        }
                    } catch (Exception e) {

                        if (e instanceof EntityUniqueKeyException) {

                            throw e;
                        } else {

                            throw new EntitySaveException("处理唯一key失败", e);
                        }
                    }
                }
            }

            uniquekeyForDb(temp, entity);
        }
    }

    /**
     * 查询数据库
     */
    private void uniquekeyForDb(Map<String, Map<String, UniqueKeyPropertis>> temp, T entity) {

        if (MyUtils.mapIsNotNull(temp)) {

            QueryWrapper<T> queryWrapper = null;

            for (String key : temp.keySet()) {

                // 查询唯一索引结果
                queryWrapper = new QueryWrapper<>();

                Map<String, Object> map = this.buildMap(temp.get(key));

                if (MyUtils.mapIsNotNull(map)) {

                    if (StringUtils.checkValNotNull(entity.getId())) {

                        queryWrapper.ne("id", entity.getId());
                    }

                    queryWrapper.allEq(map);

                    if (count(queryWrapper) > 0) {

                        String msg = getMapFristVaule(temp.get(key));

                        msg = StrUtil.isBlank(msg) ? "唯一键重复" : msg;

                        throw new EntityUniqueKeyException(msg);
                    }
                }
            }
        }
    }

    private String getMapFristVaule(Map<String, UniqueKeyPropertis> temp) {

        for (String key : temp.keySet()) {

            if (temp.get(key) != null && StrUtil.isNotBlank(temp.get(key).getMessage())) {

                return temp.get(key).getMessage();
            }
        }

        return "";
    }

    private Map<String, Object> buildMap(Map<String, UniqueKeyPropertis> propertisMap) {

        Map<String, Object> result = new HashMap<>();

        if (MyUtils.mapIsNotNull(propertisMap)) {

            for (String key : propertisMap.keySet()) {

                result.put(key, propertisMap.get(key).getValue());
            }
        }

        return result;
    }

    /**
     * 使用自定义生成的key和填充字段
     */
    private void settingInfo(T entity) {

        BasePermDO basePermDO = null;

        if (entity instanceof BasePermDO) {

            basePermDO = (BasePermDO) entity;
        }

        User user = UserUtlis.getLoginUserInfo();
        Date date = MyUtils.generateTime();
        if (!StringUtils.isNotEmpty(entity.getId())) {

            entity.setId(GenerateIdHelper.idGenerate());
            entity.setCreateTime(date);
            entity.setBeDeleted(GlobalConstant.BE_DELETE_NO);

            if (user != null) {

                entity.setCreateBy(user.getLoginName());
            } else {

                entity.setCreateBy(GlobalConstant.SYSTEM);
            }
        }

        if (user != null) {

            if (basePermDO != null) {

                if (StringUtils.checkValNull(basePermDO.getOrgId())) {

                    basePermDO.setPlatformId(user.getPlatformId());
                }

                if (StringUtils.checkValNull(basePermDO.getOrgId())) {

                    basePermDO.setOrgId(user.getOrgId());
                }
            }

            entity.setUpdateBy(user.getLoginName());
        } else {
            entity.setUpdateBy(GlobalConstant.SYSTEM);
        }

        entity.setUpdateTime(date);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        Assert.notEmpty(entityList, "error: entityList must not be empty");
        Class<?> cls = currentModelClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (T entity : entityList) {

                Object idVal = ReflectionKit.getMethodValue(cls, entity, keyProperty);
                if (StringUtils.checkValNull(idVal)) {
                    settingInfo(entity);
                    batchSqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), entity);
                } else {
                    MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                    param.put(Constants.ENTITY, entity);
                    settingInfo(entity);
                    batchSqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), param);
                }

                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    @Override
    public boolean removeById(Serializable id) {
        return SqlHelper.delBool(baseMapper.deleteById(id));
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        Assert.notEmpty(columnMap, "error: columnMap must not be empty");
        return SqlHelper.delBool(baseMapper.deleteByMap(columnMap));
    }

    @Override
    public boolean remove(Wrapper<T> wrapper) {

        return SqlHelper.delBool(baseMapper.delete(wrapper));
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return SqlHelper.delBool(baseMapper.deleteBatchIds(idList));
    }

    @Override
    public boolean updateById(T entity) {
        this.settingInfo(entity);
        return retBool(baseMapper.updateById(entity));
    }

    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {

        this.settingInfo(entity);

        return retBool(baseMapper.update(entity, updateWrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        Assert.notEmpty(entityList, "error: entityList must not be empty");
        String sqlStatement = sqlStatement(SqlMethod.UPDATE_BY_ID);
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (T anEntityList : entityList) {
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, anEntityList);
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    @Override
    public T getById(Serializable id) {

        return getById(id, true);
    }

    /**
     * 根据id查询记录
     *
     * @param id
     * @param throwException true 为空抛出异常，false 返回null
     * @return
     */
    @Override
    public T getById(Serializable id, boolean throwException) {

        T t = baseMapper.selectById(id);

        if (t == null && throwException) {
            throw new BusinessException("记录不存在");
        }
        return t;
    }

    @Override
    public Collection<T> listByIds(Collection<? extends Serializable> idList) {
        return baseMapper.selectBatchIds(idList);
    }

    @Override
    public Collection<T> listByMap(Map<String, Object> columnMap) {
        return baseMapper.selectByMap(columnMap);
    }

    @Override
    public T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        if (throwEx) {
            return baseMapper.selectOne(queryWrapper);
        }
        return SqlHelper.getObject(baseMapper.selectList(queryWrapper));
    }

    @Override
    public Map<String, Object> getMap(Wrapper<T> queryWrapper) {
        return SqlHelper.getObject(baseMapper.selectMaps(queryWrapper));
    }

    @Override
    public int count(Wrapper<T> queryWrapper) {
        return SqlHelper.retCount(baseMapper.selectCount(queryWrapper));
    }

    @Override
    public List<T> list(Wrapper<T> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper) {
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        return baseMapper.selectMaps(queryWrapper);
    }

    @Override
    public <V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return baseMapper.selectObjs(queryWrapper).stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
    }

    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<T> page, Wrapper<T> queryWrapper) {
        return baseMapper.selectMapsPage(page, queryWrapper);
    }

    @Override
    public boolean deleteBy(T object) {

        Assert.notEmpty(object.getId(), "error: id is empty");

        this.settingInfo(object);

        object.setBeDeleted(GlobalConstant.BE_DELETE_YES);

        return retBool(baseMapper.updateById(object));
    }

    @Override
    public boolean deleteBy(String id) {

        Assert.notNull(id, "error: id is empty");

        T t = this.getById(id);

        this.settingInfo(t);

        t.setBeDeleted(GlobalConstant.BE_DELETE_YES);

        return retBool(baseMapper.updateById(t));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBy(Collection<? extends Serializable> ids, boolean throwException) {

        Assert.notNull(ids, "error: ids is empty");

        List<T> results = (List<T>) this.listByIds(ids);

        // 根据id查询结果为空或者传入的集合长度和查询的结果长度不一致，直接抛出异常
        boolean checkResult = MyUtils.listIsNull(results) || results.size() != ids.size();

        if (throwException) {

            Assert.isFalse(checkResult, "删除记录部分不存在");
        }

        return deleteByRecord(results);
    }

    /**
     * 删除记录
     *
     * @param results 数据集合
     * @return
     */
    private boolean deleteByRecord(List<T> results) {

        if (MyUtils.listIsNotNull(results)) {

            for (T t : results) {

                t.setBeDeleted(GlobalConstant.BE_DELETE_YES);
            }

            return updateBatch(results);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBy(Collection<? extends Serializable> ids) {

        return this.deleteBy(ids, false);
    }

    /**
     * 根据条件逻辑删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBy(Wrapper<T> queryWrapper) {

        List<T> results = this.list(queryWrapper);

        return deleteByRecord(results);
    }

    @Override
    public PageInfo<T> page(PageBase pageBase, Wrapper<T> queryWrapper) {

        Page<T> page = PageHelper.startPage(pageBase.getCurrentPage(), pageBase.getPageSize());
        this.baseMapper.selectList(queryWrapper);
        PageInfo<T> pageInfo = new PageInfo<>(page);


        return pageInfo;
    }

    /**
     * 填充外键关联数据
     */
    private void relevanceRecord(List<T> tList) {

        if (MyUtils.listIsNull(tList)) {

            return;
        }

        Class cls = tList.get(0).getClass();

        // 获取当前对象下面的注解
        List<Field> fieldList = ReflectionKit.getFieldList(ClassUtils.getUserClass(cls));

        Map<String, RelevanceFillProperties> fillMap = new HashMap<>();

        for (Field field : fieldList) {

            RelevanceFill relevanceFill = field.getAnnotation(RelevanceFill.class);
            if (relevanceFill != null) {

                Set<Object> values = new HashSet<>();

                for (T t : tList) {

                    Object value = ReflectionKit.getMethodValue(t, field.getName()).toString();

                    values.add(value);
                }

                if (fillMap.containsKey(relevanceFill.serviceClass().getName())) {

                    fillMap.get(relevanceFill.serviceClass().getName()).getSearchValue().add(values);
                } else {

                    RelevanceFillProperties propertis = new RelevanceFillProperties();

                    propertis.setServiceClass(relevanceFill.serviceClass());
                    propertis.setRelevanceKey(relevanceFill.relevanceKey());
                    propertis.setFillProperties(relevanceFill.fillProperties());
                    propertis.setThrowException(relevanceFill.throwException());
                    propertis.setPropertiesName(field.getName());
                    propertis.setResultClass(relevanceFill.resultClass());

                    propertis.setSearchValue(values);

                    fillMap.put(propertis.getServiceClass().getName(), propertis);
                }
            }
        }

        if (MyUtils.mapIsNotNull(fillMap)) {

            return;
        }

        for (String key : fillMap.keySet()) {

            RelevanceFillProperties fillProperties = fillMap.get(key);

            BaseService<Object> service = (BaseService) SpringContextUtil.getBean(fillProperties.getServiceClass());

            List<Object> queryList = new ArrayList<>(fillProperties.getSearchValue());

            List<Map<String, Object>> resultMap = service.listMaps(new QueryWrapper<Object>().in(fillProperties.getRelevanceKey(), queryList));

        }
    }

    @Override
    public PageInfo<Map<String, Object>> pageMaps(PageBase pageBase, Wrapper<T> queryWrapper) {

        Page<Map<String, Object>> page = PageHelper.startPage(pageBase.getCurrentPage(), pageBase.getPageSize());
        this.baseMapper.selectMaps(queryWrapper);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(page);

        return pageInfo;
    }
}
