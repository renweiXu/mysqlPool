package com.pool;

import ch.qos.logback.core.util.TimeUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xurw
 * @description .
 * @date 2019/7/21
 */
public class MysqlPoolImpl implements MysqlPool {

    //最大连接数
    private  int maxSize = 10;
    // 当前已有连接
    private AtomicInteger activeSize = new AtomicInteger(0);

    //使用容器缓冲
    //使用中的连接
    LinkedBlockingQueue<Connection> busy;
    //空闲连接
    LinkedBlockingQueue<Connection> idle;


    @Override
    public void maxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * 初始化连接池
     */
    @Override
    public void init() {
        busy = new LinkedBlockingQueue<>();
        idle = new LinkedBlockingQueue<>();
    }

    /**
     * 销毁连接
     */
    @Override
    public void destory() {

    }

    /**
     * 取一个连接
     * @return
     */
    @Override
    public Connection getCon() {
        // 1、从idle里面取出连接放入busy
        Connection con = idle.poll();
        // 2、idle有值 直接取
        if (null != con){
            // 放入busy中
            busy.offer(con);
            return con;
        }
        // 3、idle没值 若连接池没满 创建连接  此处为模拟代码 多线程要考虑线程安全
        //双重判断 先用activeSize.get() < maxSize 进行判断 保证性能
        //单独使用activeSize.incrementAndGet() < maxSize 判断也可以保证线程同步 但是性能消耗大
        if (activeSize.get() < maxSize ) {
            if (activeSize.incrementAndGet() < maxSize){
                //双重判断 保证同步
                //当前连接数小于 最大连接数 直接创建连接
                con = MysqlUtil.getConnection();
                busy.offer(con);
                return con;
            }
        }
        // 4、池满全繁忙 等待释放
        try {
            //等待10s 等到了就返回连接  没有就返回null
            con = idle.poll(10000, TimeUnit.MICROSECONDS);
            if (null == con) {
                throw new RuntimeException("获取连接超时");
            }
            busy.offer(con);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * 释放连接
     * 从busy里面把连接释放  remove
     * 把连接放入到idle里面  offer
     * @param connection
     */
    @Override
    public void release(Connection connection) {
        busy.remove(connection);
        idle.offer(connection);
    }
}
