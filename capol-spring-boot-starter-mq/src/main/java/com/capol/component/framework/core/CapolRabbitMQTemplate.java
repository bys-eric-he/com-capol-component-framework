package com.capol.component.framework.core;

import com.alibaba.fastjson.JSON;
import com.capol.component.framework.properties.RabbitMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CapolRabbitMQTemplate {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    /**
     * 发送消息
     *
     * @param uuid
     * @param message 消息
     */
    public void send(String uuid, Object message) {
        log.info("-->开始发送>>>>>>>>>>>>>>>>>> 消息编号：" + uuid);
        Map<String, Object> data = new HashMap<>();
        data.put("message_id", uuid);
        data.put("message_data", message);
        rabbitTemplate.convertAndSend(
                rabbitMqProperties.getExchange(),
                rabbitMqProperties.getSyncRouting(),
                JSON.toJSONString(data));
    }
}
