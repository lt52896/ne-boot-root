package com.ne.boot.persist.jdbc;

import org.springframework.jdbc.datasource.AbstractDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by xiezhouyan on 16-5-19.
 */
public class DialectDataSource extends AbstractDataSource {


    private javax.sql.DataSource dataSource;
    private String dialect;

    public DialectDataSource(javax.sql.DataSource dataSource, String dialect) {
        this.dataSource = dataSource;
        this.dialect = dialect;
    }

    public String getDialect() {
        return dialect;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return dataSource.getConnection(username, password);
    }
}
