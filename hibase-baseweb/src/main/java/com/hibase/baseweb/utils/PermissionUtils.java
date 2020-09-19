package com.hibase.baseweb.utils;

import com.hibase.baseweb.annotation.mybatis.DataPerm;
import com.hibase.baseweb.core.exception.dataperm.DataPermException;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;

/**
 * 自定义权限相关工具类
 *
 * @author chenfeng
 * @date 2019/5/26
 */
public class PermissionUtils {

    /**
     * 根据 StatementHandler 获取 注解对象
     */
    public static DataPerm getPermissionByDelegate(MappedStatement mappedStatement) {

        DataPerm dataPerm = null;
        try {
            String id = mappedStatement.getId();
            String className = id.substring(0, id.lastIndexOf("."));
            String methodName = id.substring(id.lastIndexOf(".") + 1, id.length());
            final Class cls = Class.forName(className);
            final Method[] method = cls.getMethods();
            for (Method me : method) {
                if (me.getName().equals(methodName) && me.isAnnotationPresent(DataPerm.class)) {
                    dataPerm = me.getAnnotation(DataPerm.class);
                }
            }
        } catch (Exception e) {

            throw new DataPermException("数据权限过滤获取注解对象异常", e);
        }

        return dataPerm;
    }
}
