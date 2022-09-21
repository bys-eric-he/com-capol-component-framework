package com.capol.component.framework.callback.base;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public abstract class MsgSendConfirmCallBack implements RabbitTemplate.ConfirmCallback {
    /**
     * confirmCallback 不管消息是否成功到达交换机都会被调用，
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息消费成功！");
        } else {
            log.info("消息消费失败:" + cause + "\n重新发送！");
        }

        this.callback(correlationData, ack, cause);
    }

    /**
     * 业务方实现该回调方法处理逻辑
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    public abstract void callback(CorrelationData correlationData, boolean ack, String cause);
}