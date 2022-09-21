package com.capol.component.framework.config;

import com.capol.component.framework.properties.RabbitMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(RabbitMqProperties.class)
public class CapolMQAutoConfiguration {
    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Bean
    @ConditionalOnMissingBean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(rabbitMqProperties.getHost());
        cachingConnectionFactory.setUsername(rabbitMqProperties.getUsername());
        cachingConnectionFactory.setPassword(rabbitMqProperties.getPassword());
        cachingConnectionFactory.setPort(rabbitMqProperties.getPort());
        log.info("--------------配置RabbitMQ连接-------------");
        log.info(rabbitMqProperties.toString());
        log.info("------------------------------------------");
        // 确认开启ConfirmCallback回调
        cachingConnectionFactory.setPublisherReturns(true);
        // 新版本开启 ReturnCallback
        cachingConnectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        return cachingConnectionFactory;
    }
}
