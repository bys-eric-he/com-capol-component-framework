package com.capol.component.framework.utils;


import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * Bean注册工具类
 */
public class BeanRegisterUtil {

    public static BeanFactory beanFactory;

    /**
     * bean注册
     *
     * @param defaultListableBeanFactory
     * @param beanClass
     */
    public static void register(DefaultListableBeanFactory defaultListableBeanFactory, Class<?> beanClass) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
        defaultListableBeanFactory.registerBeanDefinition(beanClass.getName(), beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * @return
     */
    public static DefaultListableBeanFactory getDefaultListableBeanFactory() {
        return (DefaultListableBeanFactory) BeanRegisterUtil.beanFactory;
    }


    public static void setBeanFactory(BeanFactory beanFactory) {
        BeanRegisterUtil.beanFactory = beanFactory;
    }
}
