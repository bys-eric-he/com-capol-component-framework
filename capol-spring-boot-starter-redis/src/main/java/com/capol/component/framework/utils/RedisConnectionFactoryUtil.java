package com.capol.component.framework.utils;


import com.capol.component.framework.adapter.RedisAdapter;
import com.capol.component.framework.constants.RedisModeConstants;
import com.capol.component.framework.context.RedisBeanContext;
import com.capol.component.framework.core.RedisEnvironment;
import com.capol.component.framework.properties.RedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * redis 连接工厂工具类
 */
@Slf4j
public class RedisConnectionFactoryUtil {

    /**
     * 本地host
     */
    private static final String LOCAL_HOST = "localhost";

    /**
     * 本地ip
     */
    private static final String LOCAL_IP = "127.0.0.1";

    /**
     * 初始化redis连接工厂
     *
     * @param redisProperties redis配置信息
     * @return JedisConnectionFactory
     */
    public static JedisConnectionFactory init(RedisProperties redisProperties) {

        RedisEnvironment environment = RedisBeanContext.getBean(RedisEnvironment.class);

        if (!environment.getRedisConnectionFactoryIsInit()) {
            return new JedisConnectionFactory();
        }

        String redisMode = RedisBeanContext
                .getBean(RedisAdapter.class)
                .getRedisMode();

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisProperties.getMaxActive());
        poolConfig.setMaxIdle(redisProperties.getMaxIdle());
        poolConfig.setMaxWaitMillis(redisProperties.getMaxWait());
        poolConfig.setMinIdle(redisProperties.getMinIdle());
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(true);

        log.info("Redis参数配置信息 -> {} ", redisProperties.toString());

        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .usePooling()
                .poolConfig(poolConfig)
                .and()
                .readTimeout(Duration.ofMillis(redisProperties.getTimeOut()))
                .build();

        //redis连接工厂
        JedisConnectionFactory jedisConnectionFactory;

        switch (redisMode) {
            case RedisModeConstants.REDIS_SINGLE:
                RedisStandaloneConfiguration singleRedisConfig = RedisConnectionFactoryUtil
                        .getRedisSingleConfiguration(redisProperties);
                jedisConnectionFactory = new JedisConnectionFactory(singleRedisConfig, clientConfig);
                break;
            case RedisModeConstants.REDIS_CLUSTER:
                RedisClusterConfiguration clusterRedisConfig = RedisConnectionFactoryUtil
                        .getRedisClusterConfiguration(redisProperties);
                jedisConnectionFactory = new JedisConnectionFactory(clusterRedisConfig, clientConfig);
                break;
            case RedisModeConstants.REDIS_SENTINEL:
                RedisSentinelConfiguration sentinelRedisConfig = RedisConnectionFactoryUtil
                        .getRedisSentinelConfiguration(redisProperties);
                jedisConnectionFactory = new JedisConnectionFactory(sentinelRedisConfig, clientConfig);
                break;
            default:
                log.error("Redis初始化失败!!! Initialization of redis connection factory failed. " +
                        "Please specify that redis mode of operation is optional {single, cluster, sentinel}");
                throw new BeanInitializationException("Redis初始化失败!!! Initialization of redis connection factory failed");
        }

        log.info("Redis初始化完成, Initialization of redis connection factory success!!!");

        return jedisConnectionFactory;
    }


    /**
     * 获取redis单点配置
     *
     * @param redisProperties redis配置信息
     * @return RedisStandaloneConfiguration
     */
    private static RedisStandaloneConfiguration getRedisSingleConfiguration(RedisProperties redisProperties) {

        if (LOCAL_HOST.equals(redisProperties.getHost()) || LOCAL_IP.equals(redisProperties.getHost())) {
            log.warn("注意: 当前为本地连接!!!  Note that redis connection address is localhost. Change address if production environment");
        }

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisProperties.getHost());
        if (StringUtils.isEmpty(redisProperties.getPassword())) {
            redisConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }
        redisConfig.setPort(redisProperties.getPort());
        redisConfig.setDatabase(redisProperties.getDatabase());
        log.info("redis单点配置完成!!!");
        return redisConfig;
    }

    /**
     * 获取redis集群配置
     *
     * @param redisProperties redis配置信息
     * @return RedisClusterConfiguration
     */
    private static RedisClusterConfiguration getRedisClusterConfiguration(RedisProperties redisProperties) {

        if (StringUtils.isEmpty(redisProperties.getCluster())) {
            log.error("For redis cluster mode, please make cluster address Configuration name {}", "etc.redis.cluster");
            throw new BeanInitializationException("For redis cluster mode, please make cluster address");
        }

        RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
        List<RedisNode> nodeList = new ArrayList<>();
        //获取节点
        String[] cNodes = redisProperties.getCluster().split(",");
        //分割出集群节点
        splitNode(nodeList, cNodes);
        redisConfig.setClusterNodes(nodeList);
        if (!StringUtils.isEmpty(redisProperties.getPassword())) {
            redisConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }
        log.info("redis集群配置完成!!!");
        return redisConfig;
    }

    /**
     * 获取redis哨兵配置
     *
     * @param redisProperties redis配置信息
     * @return RedisSentinelConfiguration
     */
    private static RedisSentinelConfiguration getRedisSentinelConfiguration(RedisProperties redisProperties) {

        if (StringUtils.isEmpty(redisProperties.getMaster()) || StringUtils.isEmpty(redisProperties.getNodes())) {
            log.error("For redis sentinel mode, please make sentinel address Configuration name {},{} "
                    , "etc.redis.master", "etc.redis.nodes");
            throw new BeanInitializationException("For redis sentinel mode, please make sentinel address");
        }

        RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();
        redisConfig.setMaster(redisProperties.getMaster());
        List<RedisNode> nodeList = new ArrayList<>();
        //获取节点
        String[] cNodes = redisProperties.getNodes().split(",");
        //分割出集群节点
        splitNode(nodeList, cNodes);
        redisConfig.setSentinels(nodeList);
        if (!StringUtils.isEmpty(redisProperties.getPassword())) {
            redisConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }
        log.info("redis哨兵配置完成!!!");
        return redisConfig;
    }

    /**
     * 切割集群节点
     *
     * @param nodeList 节点列表
     * @param cNodes   节点
     */
    private static void splitNode(List<RedisNode> nodeList, String[] cNodes) {

        for (String node : cNodes) {
            String[] hp = node.split(":");
            nodeList.add(new RedisNode(hp[0], Integer.parseInt(hp[1])));
        }
    }
}
