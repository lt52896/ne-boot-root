package com.ne.boot.persist.jdbc;


import com.ne.boot.common.exception.NEError;
import com.ne.boot.common.exception.NEException;
import com.ne.boot.persist.jdbc.dialect.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiezhouyan on 15-12-27.
 */
public class DialectProvider {

    private static final Map<String, Dialect> DIALECTS = new HashMap<String, Dialect>();

    static {
        DIALECTS.put("DERBY", new DerbyDialect());
        DIALECTS.put("ORACLE", new OracleDialect());
        DIALECTS.put("MYSQL", new MySQLDialect());
        DIALECTS.put("SQLSERVER", new SqlServerDialect());
        DIALECTS.put("IMPALA", new ImpalaDialect());
    }

    public static Dialect get(String key) {
        if (key != null) {
            key = key.toUpperCase();
        }
        if (!DIALECTS.containsKey(key)) {
            throw new NEException(NEError.SYSTEM_INTERNAL_ERROR, "dialect " + key + " is not supported");
        }
        return DIALECTS.get(key);
    }
}
