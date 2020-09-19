package com.hibase.baseweb.core.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.hibase.baseweb.model.web.PageBase;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 父类service接口
 *
 * @author chenfeng
 * @date 2019/03/21
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 根据对象逻辑删除
     *
     * @param object
     * @return
     */
    boolean deleteBy(T object);

    /**
     * 根据条件id逻辑删除
     *
     * @param id
     * @return
     */
    boolean deleteBy(String id);

    /**
     * 根据条件id集合逻辑删除
     *
     * @param ids
     * @param throwException 是否开启校验，校验不通过抛出异常
     * @return
     */
    boolean deleteBy(Collection<? extends Serializable> ids, boolean throwException);

    /**
     * 逻辑批量删除，未开启校验，不抛出异常
     *
     * @param ids
     * @return
     */
    boolean deleteBy(Collection<? extends Serializable> ids);

    /**
     * 根据条件逻辑删除
     *
     * @param queryWrapper 查询参数
     * @return
     */
    boolean deleteBy(Wrapper<T> queryWrapper);

    @Override
    @Deprecated
    boolean remove(Wrapper<T> queryWrapper);

    @Override
    @Deprecated
    boolean removeById(Serializable id);

    @Override
    @Deprecated
    boolean removeByIds(Collection<? extends Serializable> idList);

    @Deprecated
    @Override
    boolean removeByMap(Map<String, Object> columnMap);

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    default boolean updateBatchById(Collection<T> entityList) {
        return updateBatchById(entityList, 1000);
    }

    @Override
    @Deprecated
    boolean updateBatchById(Collection<T> entityList, int batchSize);

    /**
     * 批量修改
     *
     * @param entityList
     * @param batchSize
     * @return
     */
    boolean updateBatch(Collection<T> entityList, int batchSize);

    /**
     * 批量修改
     *
     * @param entityList
     * @return
     */
    boolean updateBatch(Collection<T> entityList);

    /**
     * 通用保存
     *
     * @param entity
     * @return
     */
    T store(T entity);

    /**
     * 只更新信息不变更更新人及更新时间
     *
     * @param entity
     * @return
     */
    boolean updateOnlyById(T entity);

    /**
     * 批量保存
     *
     * @param entityList
     * @param batchSize 批量大小
     * @return
     */
    boolean storeBatch(Collection<T> entityList, int batchSize);

    /**
     * 批量保存
     *
     * @param entityList
     * @return
     */
    boolean storeBatch(Collection<T> entityList);

    /**
     * 根据id获取条数，大于0返回true
     *
     * @param id
     * @return
     */
    boolean countById(Serializable id);

    /**
     * 通过id获取记录
     *
     * @param id
     * @return
     */
    @Override
    T getById(Serializable id);

    /**
     * 通过id获取记录
     *
     * @param id
     * @param throwException true 为空抛出异常，false 返回null
     * @return
     */
    T getById(Serializable id, boolean throwException);

    /**
     * pagehelper分页
     *
     * @param pageBase
     * @param queryWrapper
     * @return
     */
    PageInfo<T> page(PageBase pageBase, Wrapper<T> queryWrapper);

    PageInfo<Map<String, Object>> pageMaps(PageBase pageBase, Wrapper<T> queryWrapper);
}
