package com.ne.boot.autoconfigure.persist.hbase;

import com.ne.boot.persist.hbase.HBaseTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xiezhouyan on 17-4-10.
 */
@Configuration
@ConditionalOnClass(HBaseTemplate.class)
public class HbaseAutoConfiguration {

    @Bean
    @ConditionalOnProperty("spring.datasource.hbase.zookeeper.quorum")
    @ConditionalOnMissingBean
    public HBaseTemplate getInstance(@Value("${spring.datasource.hbase.zookeeper.quorum}") String zkQuorum) {
        HBaseTemplate template = new HBaseTemplate();
        template.setEncoding("UTF-8");
        template.setZkQuorum(zkQuorum);
        return template;
    }
}
