package com.hibase.baseweb.config.mybatis;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.injector.AbstractLogicMethod;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 自定义拼接修改idsql
 *
 * @author chenfeng
 * @date 2019/03/26
 */
public class MyLogicUpdateById extends AbstractLogicMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        boolean logicDelete = tableInfo.isLogicDelete();
        SqlMethod sqlMethod = SqlMethod.UPDATE_BY_ID;
        StringBuilder append = new StringBuilder("<if test=\"et instanceof java.util.Map\">")
                .append("<if test=\"et.").append(OptimisticLockerInterceptor.MP_OPTLOCK_VERSION_ORIGINAL).append("!=null\">")
                .append(" AND ${et.").append(OptimisticLockerInterceptor.MP_OPTLOCK_VERSION_COLUMN)
                .append("}=#{et.").append(OptimisticLockerInterceptor.MP_OPTLOCK_VERSION_ORIGINAL).append(StringPool.RIGHT_BRACE)
                .append("</if></if>");
        if (logicDelete) {
            append.append(tableInfo.getLogicDeleteSql(true, false));
        }
        // 修改不过滤逻辑删除字段
        sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(),
                sqlSet(false, false, tableInfo, false, ENTITY, ENTITY_DOT),
                tableInfo.getKeyColumn(), ENTITY_DOT + tableInfo.getKeyProperty(),
                append);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource);
    }
}
