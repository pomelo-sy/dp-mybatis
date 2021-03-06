package com.dp.mybatis.v2.statement;

import com.dp.mybatis.v2.binding.MapperData;
import com.dp.mybatis.v2.config.DpConfiguration;
import com.dp.mybatis.v2.result.ResultSetHandler;

import java.sql.*;

/**
 * <p>Description:</p>
 * Created with IDEA
 * author:hudepin
 * createTime:2018/5/15 15:35
 */
public class DpSimpleStatementHandler implements StatementHandler{
    private final DpConfiguration configuration;
    private ResultSetHandler resultSetHandler;

    public DpSimpleStatementHandler(DpConfiguration configuration) {
        this.configuration = configuration;
        resultSetHandler = new ResultSetHandler(configuration);
    }
    @Override
    public <E> E query(MapperData mapperData, Object parameter) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(String.format(mapperData.getSql(),Integer.valueOf(String.valueOf(parameter))));
            ResultSet rs = ps.executeQuery();
            resultSetHandler=(ResultSetHandler)configuration.getInterceptorChain().pluginAll(resultSetHandler);
            return resultSetHandler.handler(rs,mapperData.getType());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Connection getConnection() throws SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/gp?useUnicode=true&characterEncoding=utf-8&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String username = "root";
        String password = "123456";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
