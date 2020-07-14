package com.ne.boot.autoconfigure.persist.mybatis;

import com.ne.boot.persist.mybatis.plugin.IdentityInterceptor;
import com.ne.boot.persist.mybatis.plugin.PaginationInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xiezhouyan on 17-3-16.
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
public class MybatisAutoConfiguration {

    @Value("${spring.datasource.dialect:MYSQL}")
    private String dialect;

    @Bean
    @ConditionalOnMissingBean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor interceptor = new PaginationInterceptor();
        interceptor.setDialect(dialect);
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public IdentityInterceptor identityInterceptor() {
        IdentityInterceptor interceptor = new IdentityInterceptor();
        interceptor.setDialect(dialect);
        return interceptor;
    }
}
