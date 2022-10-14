package com.capol.component.framework.idempotent.config;

import com.capol.component.framework.boot.RedisInitialization;
import com.capol.component.framework.config.CapolRedisTemplate;
import com.capol.component.framework.idempotent.core.aop.IdempotentAspect;
import com.capol.component.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import com.capol.component.framework.idempotent.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import com.capol.component.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import com.capol.component.framework.idempotent.core.redis.IdempotentRedisDAO;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RedisInitialization.class)
public class CapolIdempotentConfiguration {
    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(CapolRedisTemplate capolRedisTemplate) {
        return new IdempotentRedisDAO(capolRedisTemplate);
    }

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }

}
