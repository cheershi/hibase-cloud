package com.hibase.baseweb.config.mybatis;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.injector.AbstractLogicMethod;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.hibase.baseweb.constant.mybatis.HibaseSqlMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 自定义拼接修改idsql
 *
 * @author chenfeng
 * @date 2019/03/26
 */
public class MyLogicUpdateNullById extends AbstractLogicMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        boolean logicDelete = tableInfo.isLogicDelete();
        HibaseSqlMethod sqlMethod = HibaseSqlMethod.UPDATE_NULL_BY_ID;
        StringBuilder append = new StringBuilder("<if test=\"et instanceof java.util.Map\">")
                .append(" AND ${et.").append(OptimisticLockerInterceptor.MP_OPTLOCK_VERSION_COLUMN)
                .append("}=#{et.").append(OptimisticLockerInterceptor.MP_OPTLOCK_VERSION_ORIGINAL).append(StringPool.RIGHT_BRACE)
                .append("</if>");
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
