package com.capol.component.framework.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 用于方法、属性作用描述
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface Describe {
    String value() default "";
}
