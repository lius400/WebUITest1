package com.liuchao.utils;

import lombok.extern.slf4j.Slf4j;
import java.sql.*;
import java.util.*;

/**
 *数据库链接配置
 */
@Slf4j
public class MysqlUtil implements Iterator<Object[]> {

    ResultSet result;  //结果集
    List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();  //生成存放结果集的list
    int rowNum=0;     //总行数
    int curRowNo=0;   //当前行数

    public MysqlUtil(String ip, String port, String baseName,
                                 String userName, String password, String sql) throws ClassNotFoundException, SQLException {

        log.info("获取连接");
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", ip, port, baseName);
        //获取连接
        Connection conn = DriverManager.getConnection(url, userName, password);
        //获取创建语句对象
        Statement stmt = conn.createStatement();
        //执行sql语句，获取查询结果集
        result = stmt.executeQuery(sql);
        //获取当前行数据
        ResultSetMetaData rd = result.getMetaData();

        //循环每行
        while (result.next()) {
            Map<String, String> map = new HashMap<String, String>();
            //循环每列，如果不要id，则讲i设为2
            for (int i = 1; i <= rd.getColumnCount(); i++) {
                String key = result.getMetaData().getColumnName(i);
                String value = result.getString(i);
                map.put(key,value);
            }
            dataList.add(map);
        }

        this.rowNum = dataList.size();

        conn.close();
        stmt.close();
    }


    public boolean hasNext() {
        return rowNum != 0 && curRowNo < rowNum;
    }

    public Object[] next() {
        Map<String,String> s=dataList.get(curRowNo);
        Object[] d=new Object[1];
        d[0]=s;
        this.curRowNo++;
        return d;
    }

    public void remove() {
        throw new UnsupportedOperationException("remove unsupported");
    }
}