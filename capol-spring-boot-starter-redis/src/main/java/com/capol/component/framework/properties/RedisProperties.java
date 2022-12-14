package com.capol.component.framework.properties;

import com.capol.component.framework.annotation.Describe;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis配置信息
 */
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "capol.redis")
public class RedisProperties {

    @Describe(value = "redis 单点地址")
    private String host = "localhost";

    @Describe(value = "redis 集群地址")
    private String cluster;

    @Describe(value = "redis 哨兵主")
    private String master;

    @Describe(value = "redis 哨兵从节点")
    private String nodes;

    @Describe(value = "redis 端口")
    private Integer port = 6379;

    @Describe(value = "redis 数据库")
    private Integer database = 0;

    @Describe(value = "redis 密码")
    private String password;

    @Describe(value = "redis 读取超时时间")
    private Long timeOut = 5000L;

    @Describe(value = "redis 最大连接数")
    private Integer maxActive = 200;

    /**
     * 默认的值为-1，表示无限等待。
     */
    @Describe(value = "redis 获取连接时的最大等待毫秒数")
    private Long maxWait = BaseObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS;

    @Describe(value = "redis 最大空闲连接数")
    private Integer maxIdle = GenericObjectPoolConfig.DEFAULT_MAX_IDLE;

    @Describe(value = "redis 最小空闲连接数")
    private Integer minIdle = GenericObjectPoolConfig.DEFAULT_MIN_IDLE;

    @Override
    public String toString() {
        return String.format("Host: %s, cluster: %s, master: %s, nodes: %s, port: %d, database: %d, password: %s, timeOut: %d maxActive: %d, maxWait: %d, maxIdle: %d, minIdle: %d",
                host,
                cluster,
                master,
                nodes,
                port,
                database,
                password,
                timeOut,
                maxActive,
                maxWait,
                maxIdle,
                minIdle);
    }
}
