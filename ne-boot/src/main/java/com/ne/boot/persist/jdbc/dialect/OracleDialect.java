package com.ne.boot.persist.jdbc.dialect;

import com.ne.boot.common.exception.NEError;
import com.ne.boot.common.exception.NEException;
import com.ne.boot.persist.jdbc.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleDialect implements Dialect {

    private final static Logger logger = LoggerFactory.getLogger(OracleDialect.class);

    public String getPageString(String sql, Long offset, Long limit) {

        sql = sql.trim();
        boolean isForUpdate = false;
        if (sql.toLowerCase().endsWith(" for update")) {
            sql = sql.substring(0, sql.length() - 11);
            isForUpdate = true;
        }

        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);

        pagingSelect
                .append("select * from ( select row_.*, rownum rownum_ from ( ");

        pagingSelect.append(sql);

        pagingSelect.append(" ) row_ ) where rownum_ > " + offset
                + " and rownum_ <= " + (offset + limit));

        if (isForUpdate) {
            pagingSelect.append(" for update");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Generated Pager Sql is "
                    + pagingSelect.toString().replaceAll("\r|\n", ""));
        }
        return pagingSelect.toString();
    }

    @Override
    public String getLimitString(Long offset, Long limit) {
        throw new NEException(NEError.NOT_SUPPORT, " oracle get limit sql is not support");
    }

    @Override
    public String getCountString(String sql) {
        StringBuffer countSql = new StringBuffer(sql.length() + 100);
        countSql.append("select count(1) from (").append(sql).append(") t");
        if (logger.isDebugEnabled()) {
            logger.debug("Generated Count Sql is "
                    + countSql.toString().replaceAll("\r|\n", ""));
        }
        return countSql.toString();
    }

    @Override
    public String getIdentityString() {
        throw new NEException(NEError.NOT_SUPPORT, " oracle identity is not support");
    }

}
