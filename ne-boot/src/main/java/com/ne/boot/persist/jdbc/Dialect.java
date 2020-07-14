package com.ne.boot.persist.jdbc;

public interface Dialect {

    /**
     * 获取分页SQL
     *
     * @param sql
     * @param offset
     * @param limit
     * @return
     */
    String getPageString(String sql, Long offset, Long limit);

    /**
     * 获取条数限制SQL
     *
     * @param offset
     * @param limit
     * @return
     */
    String getLimitString(Long offset, Long limit);

    /**
     * 获取总是SQL
     *
     * @param sql
     * @return
     */
    String getCountString(String sql);

    /**
     * 获取主键返回值SQL
     *
     * @return
     */
    String getIdentityString();
}
