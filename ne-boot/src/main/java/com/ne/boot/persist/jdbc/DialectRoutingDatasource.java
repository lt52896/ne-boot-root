package com.ne.boot.persist.jdbc;

import com.ne.boot.common.ThreadContext;
import com.ne.boot.common.exception.NEError;
import com.ne.boot.common.exception.NEException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.*;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xiezhouyan on 16-1-29.
 */

public class DialectRoutingDatasource extends AbstractRoutingDataSource implements EnvironmentAware, BeanFactoryAware {
    private static final String CONFIG_DATASOURCE_PREFIX = "spring.datasource";
    public static final String DATASOURCE_KEY = DialectRoutingDatasource.class.getName() + "_DATASOURCE_KEY";
    public static final String DATASOURCE_NAME_KEY = DialectRoutingDatasource.class.getName() + "_DATASOURCE_NAME_KEY";
    public static final String DATASOURCE_DEFAULT_KEY = DialectRoutingDatasource.class.getName() + "_DATASOURCE_DEFAULT_KEY";
    private final String[] keys;
    private final String defaultKey;
    private Environment environment;
    private BeanFactory beanFactory;
    private PropertySources propertySources;

    public DialectRoutingDatasource(String[] keys, String defaultKey) {
        this.keys = keys;
        this.defaultKey = defaultKey;
    }

    @Override
    protected DataSource determineTargetDataSource() {
        DataSource dataSource = super.determineTargetDataSource();
        ThreadContext.put(DATASOURCE_KEY, dataSource);
        return super.determineTargetDataSource();
    }

    protected Object determineCurrentLookupKey() {
        Object o = ThreadContext.get(DATASOURCE_NAME_KEY) != null ? ThreadContext.get(DATASOURCE_NAME_KEY) : DATASOURCE_DEFAULT_KEY;
        return o;
    }

    private DataSource createDataSource(String key) {
        DialectDataSourceProperties properties = new DialectDataSourceProperties();
        String dataSourcePrefix = CONFIG_DATASOURCE_PREFIX + "." + key;
        bindProperties(properties, dataSourcePrefix);
        DataSource dataSource = createDataSource(properties);
        bindProperties(dataSource, dataSourcePrefix);
        return new DialectDataSource(dataSource, properties.getDialect());
    }

    private DataSource createDataSource(DialectDataSourceProperties properties) {
        if (StringUtils.isNotBlank(properties.getJndiName())) {
            JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
            DataSource dataSource = dataSourceLookup.getDataSource(properties.getJndiName());
            return dataSource;
        }
        DataSourceBuilder factory = DataSourceBuilder
                .create(properties.getClassLoader())
                .driverClassName(properties.getDriverClassName())
                .url(properties.getUrl()).username(properties.getUsername())
                .password(properties.getPassword());
        if (properties.getType() != null) {
            factory.type(properties.getType());
        }
        return factory.build();
    }

    public Dialect getDialect() {
        DataSource dataSource = determineTargetDataSource();
        if (!(dataSource instanceof DialectDataSource)) {
            throw new NEException(NEError.CONFIG_IS_NOT_CORRECT, "dataSource is not instance TSFDatasource could't get dialect");
        }
        String dialect = ((DialectDataSource) dataSource).getDialect();
        return DialectProvider.get(dialect);
    }

    private void bindProperties(Object bean, String targetName) {
        PropertiesConfigurationFactory<Object> factory = new PropertiesConfigurationFactory<Object>(bean);
        factory.setPropertySources(this.propertySources);
        factory.setConversionService(new DefaultConversionService());
        factory.setTargetName(targetName);
        try {
            factory.bindPropertiesToTarget();
        } catch (Exception ex) {
            String targetClass = ClassUtils.getShortName(bean.getClass());
            throw new BeanCreationException("DataSource", "Could not bind properties to " + targetClass, ex);
        }
    }

    private PropertySources deducePropertySources() {
        PropertySourcesPlaceholderConfigurer configurer = getSinglePropertySourcesPlaceholderConfigurer();
        if (configurer != null) {
            // Flatten the sources into a single list so they can be iterated
            return new DialectRoutingDatasource.FlatPropertySources(configurer.getAppliedPropertySources());
        }
        if (this.environment instanceof ConfigurableEnvironment) {
            MutablePropertySources propertySources = ((ConfigurableEnvironment) this.environment)
                    .getPropertySources();
            return new DialectRoutingDatasource.FlatPropertySources(propertySources);
        }
        // empty, so not very useful, but fulfils the contract
        return new MutablePropertySources();
    }

    private PropertySourcesPlaceholderConfigurer getSinglePropertySourcesPlaceholderConfigurer() {
        // Take care not to cause early instantiation of all FactoryBeans
        if (this.beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) this.beanFactory;
            Map<String, PropertySourcesPlaceholderConfigurer> beans = listableBeanFactory
                    .getBeansOfType(PropertySourcesPlaceholderConfigurer.class, false,
                            false);
            if (beans.size() == 1) {
                return beans.values().iterator().next();
            }
        }
        return null;
    }

    private static class FlatPropertySources implements PropertySources {

        private PropertySources propertySources;

        FlatPropertySources(PropertySources propertySources) {
            this.propertySources = propertySources;
        }

        @Override
        public Iterator<PropertySource<?>> iterator() {
            MutablePropertySources result = getFlattened();
            return result.iterator();
        }

        @Override
        public boolean contains(String name) {
            return get(name) != null;
        }

        @Override
        public PropertySource<?> get(String name) {
            return getFlattened().get(name);
        }

        private MutablePropertySources getFlattened() {
            MutablePropertySources result = new MutablePropertySources();
            for (PropertySource<?> propertySource : this.propertySources) {
                flattenPropertySources(propertySource, result);
            }
            return result;
        }

        private void flattenPropertySources(PropertySource<?> propertySource,
                                            MutablePropertySources result) {
            Object source = propertySource.getSource();
            if (source instanceof ConfigurableEnvironment) {
                ConfigurableEnvironment environment = (ConfigurableEnvironment) source;
                for (PropertySource<?> childSource : environment.getPropertySources()) {
                    flattenPropertySources(childSource, result);
                }
            } else {
                result.addLast(propertySource);
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() {
        if (propertySources == null) {
            propertySources = deducePropertySources();
        }
        if (keys == null || ArrayUtils.isEmpty(keys)) {
            throw new NEException("keys is null or empty");
        }
        if (StringUtils.isBlank(defaultKey)) {
            throw new NEException("defaultkey is null or empty");
        }
        Map<Object, Object> dataSources = new HashMap<>();
        for (String key : keys) {
            DataSource dataSource = createDataSource(key);
            dataSources.put(key, dataSource);
        }
        setTargetDataSources(dataSources);
        setDefaultTargetDataSource(dataSources.get(defaultKey));
        super.afterPropertiesSet();
    }
}
