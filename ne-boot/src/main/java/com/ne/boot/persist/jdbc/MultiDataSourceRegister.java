package com.ne.boot.persist.jdbc;

import com.ne.boot.persist.jdbc.annoation.EnableMultiDataSource;
import com.ne.boot.persist.jdbc.aop.MultiDataSourceAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * Created by xiezhouyan on 16-6-14.
 */
public class MultiDataSourceRegister implements ImportBeanDefinitionRegistrar {

    private static Logger logger = LoggerFactory.getLogger(MultiDataSourceRegister.class);


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = getAttributes(importingClassMetadata, EnableMultiDataSource.class.getName());
        String[] keys = attributes.getStringArray("keys");
        String defaultKey = attributes.getString("defaultKey");
        RootBeanDefinition dataSourceBean = new RootBeanDefinition(DialectRoutingDatasource.class);
        ConstructorArgumentValues args = new ConstructorArgumentValues();
        args.addIndexedArgumentValue(0, keys);
        args.addIndexedArgumentValue(1, defaultKey);
        dataSourceBean.setConstructorArgumentValues(args);
        registry.registerBeanDefinition(DialectRoutingDatasource.class.getName(), dataSourceBean);

        AopConfigUtils.registerAutoProxyCreatorIfNecessary(registry);
        RootBeanDefinition dataSourceAdvisorBean = new RootBeanDefinition(MultiDataSourceAdvisor.class);
        dataSourceAdvisorBean.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        registry.registerBeanDefinition(MultiDataSourceAdvisor.class.getName(), dataSourceAdvisorBean);
    }

    protected AnnotationAttributes getAttributes(AnnotationMetadata metadata, String name) {
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(metadata.getAnnotationAttributes(name, true));
        Assert.notNull(attributes,
                "No auto-configuration attributes found. Is " + metadata.getClassName()
                        + " annotated with " + ClassUtils.getShortName(name) + "?");
        return attributes;
    }
}
