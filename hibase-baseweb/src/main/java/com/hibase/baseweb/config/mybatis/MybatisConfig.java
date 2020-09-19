package com.hibase.baseweb.config.mybatis;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis配置
 *
 * @author chenfeng
 * @date 2019/03/22
 */
@Configuration
public class MybatisConfig {

    /**
     * 分页插件
     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        return new PaginationInterceptor();
//    }

    /**
     * 逻辑删除
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new MyLogicSqlInjector();
    }
}
