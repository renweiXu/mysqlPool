package com.pool;

import java.sql.Connection;

/**
 * @author xurw
 * @description .
 * @date 2019/7/21
 */
public interface MysqlPool {

    //最大连接数
    void maxSize(int maxSize);

    //初始化
    void init();

    //销毁
    void destory();

    //获取连接
    Connection getCon();

    void release(Connection connection);
}
