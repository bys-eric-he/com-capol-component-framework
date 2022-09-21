package com.capol.component.framework.config;


import com.capol.component.framework.properties.RabbitMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 队列配置
 */
@Slf4j
@Configuration
public class QueueConfig {

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Bean
    public Queue syncQueue() {
        log.info("------------创建队列:{}----------", rabbitMqProperties.getSyncQueue());
        /**
         durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
         auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
         exclusive  表示该消息队列是否只在当前connection生效,默认是false
         */
        return new Queue(rabbitMqProperties.getSyncQueue(), true, false, false);
    }

    @Bean
    public Queue deleteQueue() {
        log.info("------------创建队列:{}----------", rabbitMqProperties.getDeleteQueue());
        return new Queue(rabbitMqProperties.getDeleteQueue(), true, false, false);
    }
}
