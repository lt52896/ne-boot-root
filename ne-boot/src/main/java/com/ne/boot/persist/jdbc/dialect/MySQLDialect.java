package com.ne.boot.persist.jdbc.dialect;


import com.ne.boot.persist.jdbc.Dialect;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLDialect implements Dialect {

    private final static Logger logger = LoggerFactory.getLogger(MySQLDialect.class);

    public static final int INDEX_NOT_FOUND = -1;

    private static final String COUNT_SQL_PREFIX = "-- COUNT_START";

    private static final String COUNT_SQL_SUFFIX = "-- COUNT_END";

    @Override
    public String getPageString(String sql, Long offset, Long limit) {
        sql = sql.trim();
        if (StringUtils.indexOfIgnoreCase(sql, COUNT_SQL_PREFIX) > 0) {
            StringUtils.remove(sql, COUNT_SQL_PREFIX);
        }
        if (StringUtils.indexOfIgnoreCase(sql, COUNT_SQL_SUFFIX) > 0) {
            StringUtils.remove(sql, COUNT_SQL_SUFFIX);
        }
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        pagingSelect.append(sql);

        if (offset > 0) {
            pagingSelect.append(" limit ").append(offset).append(',').append(limit);
        } else {
            pagingSelect.append(" limit ").append(limit);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Generated Pager Sql is " + pagingSelect.toString().replaceAll("\r|\n", ""));
        }
        return pagingSelect.toString();
    }

    @Override
    public String getLimitString(Long offset, Long limit) {
        StringBuffer limitSql = new StringBuffer();
        limitSql.append(" limit ").append(offset).append(" , ").append(limit);
        if (logger.isDebugEnabled()) {
            logger.debug("Generated Pager Sql is " + limitSql.toString().replaceAll("\r|\n", ""));
        }
        return limitSql.toString();
    }

    @Override
    public String getCountString(String sql) {
        StringBuffer countSql = new StringBuffer(sql.length() + 100);
        String prefix = "from";
        String suffix = "order by";
        if (StringUtils.indexOfIgnoreCase(sql, COUNT_SQL_PREFIX) > 0 && StringUtils.indexOfIgnoreCase(sql, COUNT_SQL_SUFFIX) > 0) {
            prefix = COUNT_SQL_PREFIX;
            suffix = COUNT_SQL_SUFFIX;
        }
        String subSql = substringBetween(sql, prefix, suffix);
        if (StringUtils.isBlank(subSql)) {
            countSql.append("select count(1) from (").append(sql).append(") t");
        } else {
            countSql.append("select count(1) from ").append(subSql);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Generated Count Sql is " + countSql.toString().replaceAll("\r|\n", ""));
        }
        return countSql.toString();
    }

    @Override
    public String getIdentityString() {
        return " select last_insert_id() ";
    }

    public static String substringBetween(final String str, final String open, final String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        final int start = StringUtils.indexOfIgnoreCase(str, open);
        if (start != INDEX_NOT_FOUND) {
            final int end = StringUtils.indexOfIgnoreCase(str, close, start + open.length());
            if (end != INDEX_NOT_FOUND) {
                return str.substring(start + open.length(), end);
            } else {
                return str.substring(start + open.length(), str.length());
            }
        }
        return str;
    }
}