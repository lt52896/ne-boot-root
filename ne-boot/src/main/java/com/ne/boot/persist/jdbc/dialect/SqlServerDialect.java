package com.ne.boot.persist.jdbc.dialect;

import com.ne.boot.common.exception.NEError;
import com.ne.boot.common.exception.NEException;
import com.ne.boot.persist.jdbc.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlServerDialect implements Dialect {

    private final static Logger logger = LoggerFactory.getLogger(SqlServerDialect.class);

    @Override
    public String getPageString(String sql, Long offset, Long limit) {
        sql = sql.trim();
        if (offset == 0) {
            return new StringBuilder(sql.length() + 8)
                    .append(sql)
                    .insert(getSqlAfterSelectInsertPoint(sql), " top " + limit)
                    .toString();

        }
        return getPageString(sql, offset > 0, offset, offset + limit);
    }

    public String getPageString(String sql, boolean hasOffset, Long offset, Long lastValue) {
        int orderByIndex = sql.toLowerCase().lastIndexOf("order by");

        if (orderByIndex <= 0) {
            throw new UnsupportedOperationException(
                    "must specify 'order by' statement to support limit operation with offset in sql server 2005");
        }
        String sqlOrderBy = sql.substring(orderByIndex + 8);
        String sqlRemoveOrderBy = sql.substring(0, orderByIndex);
        int insertPoint = getSqlAfterSelectInsertPoint(sql);
        return new StringBuffer(sql.length() + 100)
                .append("with pagination as(")
                .append(sqlRemoveOrderBy)
                .insert(insertPoint + 23, " ROW_NUMBER() OVER(ORDER BY " + sqlOrderBy + ") as RowNumber,")
                .append(") select * from pagination where RowNumber>")
                .append(offset).append(" and RowNumber<=").append(lastValue)
                .toString();
    }

    /**
     * 获取sql中select子句位置
     *
     * @param sql
     * @return
     */
    protected static int getSqlAfterSelectInsertPoint(String sql) {
        int selectIndex = sql.toLowerCase().indexOf("select");

        int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");

        return selectIndex + ((selectDistinctIndex == selectIndex) ? 15 : 6);
    }


    @Override
    public String getLimitString(Long offset, Long limit) {
        throw new NEException(NEError.NOT_SUPPORT, " sql server get limit sql is not support");
    }

    @Override
    public String getCountString(String sql) {
        StringBuffer countSql = new StringBuffer(sql.length() + 100);
        String lowerSq = sql.toLowerCase();
        if (lowerSq.contains("order by")) {
            sql = lowerSq.split("order by")[0];
        }
        countSql.append("select count(1) from (").append(sql).append(") t");
        if (logger.isDebugEnabled()) {
            logger.debug("Generated Count Sql is "
                    + countSql.toString().replaceAll("\r|\n", ""));
        }
        return countSql.toString();
    }

    @Override
    public String getIdentityString() {
        throw new NEException(NEError.NOT_SUPPORT, " sql server identity is not support");
    }
}