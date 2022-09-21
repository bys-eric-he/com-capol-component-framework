package com.capol.component.framework.boot;


import com.capol.component.framework.config.CapolRedisTemplate;
import com.capol.component.framework.context.RedisBeanContext;
import com.capol.component.framework.properties.RedisProperties;
import com.capol.component.framework.utils.RedisConnectionFactoryUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import javax.annotation.Resource;

/**
 * 操作bean类初始化
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisInitialization {

    @Resource
    private RedisProperties redisProperties;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public RedisBeanContext redisBeanContext() {
        return new RedisBeanContext();
    }

    @Bean
    @ConditionalOnMissingBean(CapolRedisTemplate.class)
    public CapolRedisTemplate easyRedisTemplate() {
        return new CapolRedisTemplate();
    }

    @Bean
    @ConditionalOnMissingBean(JedisConnectionFactory.class)
    public JedisConnectionFactory jedisConnectionFactory() {
        return RedisConnectionFactoryUtil.init(redisProperties);
    }
}