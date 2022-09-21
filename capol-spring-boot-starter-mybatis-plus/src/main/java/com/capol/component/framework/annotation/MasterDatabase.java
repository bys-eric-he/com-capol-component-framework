package com.capol.component.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @MasterDatabase 自定义注解
 * 经过该方法标识的service方法中的所有sql都使用主库
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MasterDatabase {
}
