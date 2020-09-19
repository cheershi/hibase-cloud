package com.hibase.baseweb.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库properties对应的table信息
 *
 * @author chenfeng
 * @date 2019/03/28
 */
@Slf4j
public class TablePropertiesHelper {

    /**
     * 储存properties对应表信息
     */
    private static final Map<String, TableFieldInfo> PROPERTIES_TABLE_CACHE = new ConcurrentHashMap<>();

    /**
     * <p>
     * 获取实体映射表信息
     * </p>
     *
     * @param properties 属性名称
     * @return 数据库表反射信息
     */
    public static TableFieldInfo getTableFieldInfo(String properties, TableInfo tableInfo) {
        if (StrUtil.isBlank(properties)) {
            return null;
        }

        if (tableInfo == null || CollUtil.isEmpty(tableInfo.getFieldList())) {

            return null;
        }

        String key = tableInfo.getTableName() + properties;

        TableFieldInfo tableFieldInfo = PROPERTIES_TABLE_CACHE.get(key);
        if (null != tableFieldInfo) {
            return tableFieldInfo;
        }

        for (TableFieldInfo temp : tableInfo.getFieldList()) {

            if (properties.equals(temp.getProperty())) {

                tableFieldInfo = temp;
                break;
            }
        }

        PROPERTIES_TABLE_CACHE.put(key, tableFieldInfo);

        return tableFieldInfo;
    }
}
