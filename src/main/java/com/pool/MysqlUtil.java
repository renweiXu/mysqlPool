package com.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author xurw
 * @description .
 * @date 2019/7/21
 */
public class MysqlUtil {

    private static String url = "jdbc:mysql://192.168.1.33:3306/test";

    private static String userName = "root";

    private static String password = "123456";

    static {
        try {
            Class.forName("com.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(url,userName,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
