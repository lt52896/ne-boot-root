package com.ne.boot.test;

import com.ne.boot.common.entity.ClientType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xiezhouyan on 16-7-7.
 */
@Configuration
public class NETestConfig {
    @Value("${ne.test.clientType:BROWSER}")
    private ClientType clientType;
}
