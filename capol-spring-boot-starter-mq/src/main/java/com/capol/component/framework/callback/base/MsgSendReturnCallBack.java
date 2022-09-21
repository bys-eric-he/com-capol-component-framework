package com.capol.component.framework.callback.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 交换机到队列的保证机制,采用 returnCallback 机制,默认不开启
 */
@Component
@Slf4j
public class MsgSendReturnCallBack implements RabbitTemplate.ReturnCallback {
    /**
     * returnCallback 只有在交换机到达队列失败的时候才会被触发
     * 当这个回调函数被调用的时候，说明交换机的消息没有顺利的到达队列
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("-->返回消息回调:{} 应答代码:{} 回复文本:{} 交换器:{} 路由键:{}", message, replyCode, replyText, exchange, routingKey);
    }
}
