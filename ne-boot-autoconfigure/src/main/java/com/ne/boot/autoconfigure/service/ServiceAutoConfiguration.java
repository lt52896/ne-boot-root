package com.ne.boot.autoconfigure.service;

import com.ne.boot.service.exception.GlobalExceptionHandler;
import com.ne.boot.service.interceptor.ThreadContextInterceptor;
import com.ne.boot.service.logging.LoggingFilter;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.RequestHandler;
import springfox.documentation.schema.CodeGenGenericTypeNamingStrategy;
import springfox.documentation.schema.DefaultGenericTypeNamingStrategy;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.servlet.Servlet;
import javax.validation.ValidatorFactory;
import java.util.List;

/**
 * Created by xiezhouyan on 17-3-16.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurerAdapter.class})
public class ServiceAutoConfiguration {

    @Configuration
    @ConditionalOnClass({HibernateValidator.class, ValidatorFactory.class})
    public static class ValidatorConfigure {
        @Bean
        @ConditionalOnMissingBean
        public Validator validator(MessageSource messageSource) {
            LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
            validatorFactoryBean.setProviderClass(HibernateValidator.class);
            validatorFactoryBean.setValidationMessageSource(messageSource);
            return validatorFactoryBean;
        }
    }

    @Configuration
    @ConditionalOnClass({Docket.class, CodeGenGenericTypeNamingStrategy.class, DefaultGenericTypeNamingStrategy.class})
    public static class Swagger2Configure {

        @Autowired
        private BeanFactory beanFactory;

        @Bean
        @ConditionalOnMissingBean
        public Docket docket() {
            List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
            Docket docket = new Docket(DocumentationType.SWAGGER_2)
                    .select().apis(input -> {
                        String name = declaringClass(input).getPackage().getName();
                        for (String pack : packages) {
                            return name.startsWith(pack);
                        }
                        return false;
                    }).build();
            return docket;
        }

        private static Class<?> declaringClass(RequestHandler input) {
            return input.declaringClass();
        }
    }


    @Bean
    @ConditionalOnMissingBean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(false);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(8);
        return messageSource;
    }


    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Configuration
    public static class ServiceMvcConfiguration extends WebMvcConfigurerAdapter {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new ThreadContextInterceptor()).addPathPatterns("/**");
            super.addInterceptors(registry);
        }
    }
}
