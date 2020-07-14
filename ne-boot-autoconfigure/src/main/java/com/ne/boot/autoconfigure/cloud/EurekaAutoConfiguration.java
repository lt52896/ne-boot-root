package com.ne.boot.autoconfigure.cloud;

import com.ne.boot.autoconfigure.service.ServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xiezhouyan on 17-4-20.
 */
@Configuration
@AutoConfigureAfter(ServiceAutoConfiguration.class)
@ConditionalOnClass(name = "org.springframework.cloud.netflix.eureka.EnableEurekaClient")
@ConditionalOnProperty(value = "eureka.client.enabled", havingValue = "true")
public class EurekaAutoConfiguration {

    @Configuration
    @EnableEurekaClient
    public static class EurekaClientAutoConfiguration {
        @Bean
        @LoadBalanced
        @Primary
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }
}
