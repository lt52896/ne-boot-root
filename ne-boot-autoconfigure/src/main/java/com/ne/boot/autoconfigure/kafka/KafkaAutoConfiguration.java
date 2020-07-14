package com.ne.boot.autoconfigure.kafka;

import com.ne.boot.kafka.KafkaConsumerManager;
import com.ne.boot.kafka.listener.AbstractListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;
import java.util.Map;

/**
 * Created by xiezhouyan on 17-4-10.
 */
@Configuration
@ConditionalOnClass({KafkaProducer.class, KafkaConsumer.class})
@ConditionalOnProperty("spring.kafka.bootstrapServers")
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaAutoConfiguration implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(KafkaAutoConfiguration.class);
    private ApplicationContext context;
    private KafkaProducer producer;
    @Autowired
    private KafkaProperties properties;
    @Autowired
    private KafkaConsumerManager consumerManager;

    @Bean
    public KafkaConsumerManager consumerManager() {
        return new KafkaConsumerManager();
    }

    @Bean
    public KafkaProducer producer() {
        producer = new KafkaProducer(properties.buildProducerProperties());
        return producer;
    }

    @PreDestroy
    public void destory() {
        if (consumerManager != null) {
            consumerManager.shutdown();
        }
        if (producer != null) {
            producer.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, KafkaProperties.Consumer> consumers = properties.getConsumers();
        if (CollectionUtils.isEmpty(consumers)) {
            logger.debug("consumers is empty");
            return;
        }
        for (String key : consumers.keySet()) {
            KafkaProperties.Consumer consumer = consumers.get(key);
            Map<String, Object> props = properties.buildConsumerProperties(consumer);
            AbstractListener listener = consumer.getListener().newInstance();
            consumerManager.createConsumer(consumer.getTaskNum(), consumer.getTopic(), props, context, listener);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
