package com.ne.boot.persist.jdbc.aop;

import com.ne.boot.persist.jdbc.annoation.MultiDataSource;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

/**
 * Created by xiezhouyan on 17-4-17.
 */
public class MultiDataSourceAdvisor extends AbstractPointcutAdvisor {

    private MultiDataSourceInterceptor interceptor = new MultiDataSourceInterceptor();
    private AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    @Override
    public Pointcut getPointcut() {
        pointcut.setExpression("@annotation(" + MultiDataSource.class.getName() + ")");
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return interceptor;
    }
}
