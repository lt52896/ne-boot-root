package com.ne.boot.persist.jdbc.aop;

import com.ne.boot.common.ThreadContext;
import com.ne.boot.persist.jdbc.DialectRoutingDatasource;
import com.ne.boot.persist.jdbc.annoation.MultiDataSource;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;

/**
 * Created by xiezhouyan on 16-1-29.
 */
public class MultiDataSourceInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = AopUtils.getTargetClass(invocation.getThis());
        final Method method = AopUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
        MultiDataSource key = method.getAnnotation(MultiDataSource.class);
        try {
            ThreadContext.put(DialectRoutingDatasource.DATASOURCE_NAME_KEY, key.value());
            Object o = invocation.proceed();
            return o;
        } finally {
            ThreadContext.remove(DialectRoutingDatasource.DATASOURCE_NAME_KEY);
        }
    }
}
