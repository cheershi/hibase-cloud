package com.hibase.baseweb.config.help;


import cn.hutool.core.util.RandomUtil;
import com.hibase.baseweb.exception.GenerateIdException;
import com.hibase.baseweb.utils.IdWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 生成id helper类
 *
 * @author hufeng
 * @date 2018/12/27
 */
@Component
public class GenerateIdHelper {

    /**
     * 机器id
     */
    private static Long workId;

    @Value("${hibase.generateId.appId:}")
    public void setWorkId(Long workId) {
        GenerateIdHelper.workId = workId;
        if(null == workId){

            GenerateIdHelper.workId = RandomUtil.randomLong(0, 31);
        }
    }

    /**
     * 数据中心id
     */
    private static Long datacenterId;

    @Value("${hibase.generateId.computerId:}")
    public void setDatacenterId(Long datacenterId) {
        GenerateIdHelper.datacenterId = datacenterId;
        if(null == datacenterId){

            GenerateIdHelper.datacenterId = RandomUtil.randomLong(0, 31);
        }
    }

    /**
     * 生成id
     */
    public static String idGenerate() {

        try {

            return String.valueOf(IdWorker.getInstance(workId, datacenterId).nextId());
        } catch (Exception e) {

            throw new GenerateIdException("generate id failMsg. Cause: " + e, e);
        }
    }
}
