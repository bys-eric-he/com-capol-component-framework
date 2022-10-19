package com.capol.component.framework.callback.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 交换机到队列的保证机制,采用 returnCallback 机制,默认不开启
 */
@Component
@Slf4j
public abstract class MsgSendReturnCallBack implements RabbitTemplate.ReturnsCallback {
    /**
     * returnCallback 只有在交换机到达队列失败(消息路由失败)的时候才会被触发
     * 当这个回调函数被调用的时候，说明交换机的消息没有顺利的到达队列
     * 消息（带有路由键routingKey）到达交换机, 与交换机的所有绑定的键进行匹配，匹配不到对应的队列时触发该回调
     *
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        this.callback(returnedMessage);
        log.info("-->交换机：{}", returnedMessage.getExchange());
        log.info("-->路由键：{}", returnedMessage.getRoutingKey());
        log.info("-->消息主体 : {}", returnedMessage.getMessage());
        log.info("-->回复代码 : {}", returnedMessage.getReplyCode());
        log.info("-->描述：{}", returnedMessage.getReplyText());
    }

    /**
     * 业务方实现该回调方法处理逻辑
     *
     * @param returnedMessage
     */
    public abstract void callback(ReturnedMessage returnedMessage);
}
