package com.hibase.baseweb.cache;


import com.hibase.baseweb.constant.web.GlobalConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据权限 cache
 *
 * @author chenfeng
 * @date 2019/01/17
 */
@Repository
public class DataPermCache {

    public final static String MODULE_KEY_ORG_INFO = "org-info:hash";

    public final static String MDS_BASE_KEY = "mds";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据组织id获取缓存组织表层级数据
     */
    public List<String> getOrgInfo(String orgId) {

        HashOperations<String, String, List<String>> cache = redisTemplate.opsForHash();

        String orgInfoKey = getOrgInfoKey();

        return cache.get(orgInfoKey, orgId);
    }

    /**
     * 生成组织的key
     */
    private String getOrgInfoKey() {

        StringBuilder key = new StringBuilder();

        key.append(MDS_BASE_KEY);
        key.append(GlobalConstant.SPLIT);
        key.append(MODULE_KEY_ORG_INFO);

        return key.toString();
    }
}
