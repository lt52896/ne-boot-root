package com.ne.boot.autoconfigure.persist.mybatis;

import com.ne.boot.common.exception.NEException;
import com.ne.boot.persist.mybatis.cache.RedisCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

/**
 * Created by xiezhouyan on 17-4-22.
 */
@Configuration
@ConditionalOnClass({Jedis.class, RedisTemplate.class})
@ConditionalOnProperty(value = "mybatis.cache.enabled", havingValue = "true")
@AutoConfigureAfter(RedisRepositoriesAutoConfiguration.class)
@ConditionalOnBean(name = "redisTemplate")
public class MybatisCacheAutoConfiguration implements InitializingBean {
    @Resource(name = "redisTemplate")
    private HashOperations<String, Object, byte[]> template;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (template == null) {
            throw new NEException("redisTemplate is not autowired");
        }
        RedisCache.setTemplate(template);
    }
}
