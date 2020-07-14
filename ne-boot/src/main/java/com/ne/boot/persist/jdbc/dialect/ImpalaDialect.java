package com.ne.boot.persist.jdbc.dialect;

import com.ne.boot.common.exception.NEError;
import com.ne.boot.common.exception.NEException;
import com.ne.boot.persist.jdbc.Dialect;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiezh on 2016/9/7.
 */
public class ImpalaDialect implements Dialect {
    private Logger logger = LoggerFactory.getLogger(ImpalaDialect.class);

    @Override
    public String getPageString(String sql, Long offset, Long limit) {
        sql = sql.trim();

        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        pagingSelect.append(sql);

        pagingSelect.append(" limit ").append(limit).append(" offset ").append(offset);

        if (logger.isDebugEnabled()) {
            logger.debug("Generated Pager Sql is " + pagingSelect.toString().replaceAll("\r|\n", ""));
        }
        return pagingSelect.toString();
    }

    @Override
    public String getLimitString(Long offset, Long limit) {
        StringBuffer limitSql = new StringBuffer();
        limitSql.append(" limit ").append(limit).append(" offset ").append(offset);
        if (logger.isDebugEnabled()) {
            logger.debug("Generated Pager Sql is " + limitSql.toString().replaceAll("\r|\n", ""));
        }
        return limitSql.toString();
    }

    @Override
    public String getCountString(String sql) {
        sql = StringUtils.lowerCase(sql);
        if (sql != null) {
            sql = sql.toLowerCase();
        }
        StringBuffer countSql = new StringBuffer(sql.length() + 100);
        countSql.append("select count(1) from ").append(StringUtils.substringBetween(StringUtils.lowerCase(sql), "from", "order"));
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
