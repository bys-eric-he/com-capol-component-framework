package com.capol.component.framework.annotation;

import com.capol.component.framework.constants.RedisModeConstants;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用redis注解 默认模式为单点
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RedisConfigRegistrar.class)
public @interface EnableRedis {

    String value() default RedisModeConstants.REDIS_SINGLE;

}
