package com.ne.boot.persist.jdbc;


import javax.sql.DataSource;

/**
 * Created by xiezhouyan on 16-6-18.
 */
public class DialectUtil {

    public static Dialect getDialect() {
        DataSource dataSource = DataSourceUtil.getCurrentDataSource();
        return getDialect(dataSource);
    }

    public static Dialect getDialect(DataSource dataSource) {
        if (dataSource instanceof DialectRoutingDatasource) {
            return ((DialectRoutingDatasource) dataSource).getDialect();
        }
        if (dataSource instanceof DialectDataSource) {
            String dialect = ((DialectDataSource) dataSource).getDialect();
            return DialectProvider.get(dialect);
        }
        return null;
    }
}
