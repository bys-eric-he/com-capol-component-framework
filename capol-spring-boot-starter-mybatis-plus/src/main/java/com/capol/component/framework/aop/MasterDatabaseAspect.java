package com.capol.component.framework.aop;


import com.capol.component.framework.core.DynamicDataSourceHolder;
import com.capol.component.framework.core.MasterDatabaseAnnotationLocal;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 数据源aop
 *
 */
@Aspect
public class MasterDatabaseAspect {

    @Pointcut("@annotation(com.capol.component.framework.annotation.MasterDatabase)")
    public void writePointcut() {}

    @Pointcut("execution(public * *..*.controller..*.*(..))")
    public void controllerPoint() {}

    @Before("writePointcut()")
    public void writeBefore() {
        MasterDatabaseAnnotationLocal.masterSetting();
    }

    @AfterReturning("writePointcut()")
    public void writeClose() {
        MasterDatabaseAnnotationLocal.clear();
        DynamicDataSourceHolder.clear();
    }

    @AfterReturning("controllerPoint()")
    public void destroyThreadLocal(){
        MasterDatabaseAnnotationLocal.clear();
        DynamicDataSourceHolder.clear();
    }
}