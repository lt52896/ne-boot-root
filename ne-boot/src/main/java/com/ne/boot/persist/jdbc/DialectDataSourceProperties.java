package com.ne.boot.persist.jdbc;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

/**
 * Created by xiezhouyan on 16-7-2.
 */
public class DialectDataSourceProperties extends DataSourceProperties {

    private String dialect;

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
}
