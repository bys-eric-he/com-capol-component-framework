package com.capol.component.framework.adapter;


import com.capol.component.framework.base.AbstractRedisProcessor;
import com.capol.component.framework.constants.RedisModeConstants;
import com.capol.component.framework.context.RedisBeanContext;
import com.capol.component.framework.handler.RedisClusterProcessor;
import com.capol.component.framework.handler.RedisSentinelProcessor;
import com.capol.component.framework.handler.RedisSingleProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * redis适配器，适配redis各模式处理类
 */
@Slf4j
public class RedisAdapter {

    private String redisMode;


    public RedisAdapter() {
    }

    public String getRedisMode() {
        return redisMode;
    }

    public void setRedisMode(String redisMode) {
        this.redisMode = redisMode;
    }

    /**
     * 获取redis 处理器
     *
     * @return AbstractRedisProcessor
     */
    public AbstractRedisProcessor getRedisProcessor() {
        switch (redisMode) {
            case RedisModeConstants.REDIS_SINGLE:
                return RedisBeanContext.getBean(RedisSingleProcessor.class);
            case RedisModeConstants.REDIS_CLUSTER:
                return RedisBeanContext.getBean(RedisClusterProcessor.class);
            case RedisModeConstants.REDIS_SENTINEL:
                return RedisBeanContext.getBean(RedisSentinelProcessor.class);
            default:
                log.error("Failed to obtain redis schema instance params (无法获取redis实例参数): {}", redisMode);
                throw new RuntimeException("ailed to obtain redis schema instance (无法获取redis实例) !!");
        }
    }
}