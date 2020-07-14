package com.ne.boot.persist.jdbc;


import com.ne.boot.common.ThreadContext;

import javax.sql.DataSource;

/**
 * Created by xiezhouyan on 16-2-2.
 */
public class DataSourceUtil {

    public static void switchDataSource(String key) {
        ThreadContext.put(DialectRoutingDatasource.DATASOURCE_NAME_KEY, key);
    }

    public static DataSource getCurrentDataSource() {
        DataSource dataSource = ThreadContext.get(DialectRoutingDatasource.DATASOURCE_KEY);
        return dataSource;
    }
}
