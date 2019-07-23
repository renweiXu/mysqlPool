package com.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xurw
 * @description 使用java配置类注入 bean 等价于xml
 * @date 2019/7/21
 */
@Configuration
public class MyConfig {

    @Bean
    public MysqlPool mysqlPool () {
        MysqlPool mysqlPool = new MysqlPoolImpl();
        mysqlPool.maxSize(10);
        mysqlPool.init();
        return mysqlPool;
    }
}
