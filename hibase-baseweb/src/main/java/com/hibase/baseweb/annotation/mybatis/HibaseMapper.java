package com.hibase.baseweb.annotation.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

/**
 * hibase通用mapper
 *
 * @author chenfeng
 * @date 2019/06/27
 */
public interface HibaseMapper<T> extends BaseMapper<T> {

    /**
     * 根据 ID 修改，对象中内容为空，则更新为null
     *
     * @param entity 实体对象
     * @return
     */
    int updateNullById(@Param(Constants.ENTITY) T entity);

    /**
     * 根据 条件 修改，对象中内容为空，则更新为null
     *
     * @param entity 实体对象
     * @return
     */
    int updateNull(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper);
}
