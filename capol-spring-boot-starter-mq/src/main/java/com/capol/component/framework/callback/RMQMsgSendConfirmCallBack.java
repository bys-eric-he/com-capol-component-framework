package com.capol.component.framework.callback;

import com.capol.component.framework.callback.base.MsgSendConfirmCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Component;

/**
 * 生产者到交换机的可靠保证依靠的是： confrimCallback 机制。
 */
@Component
@Slf4j
public class RMQMsgSendConfirmCallBack extends MsgSendConfirmCallBack {
    /**
     * 业务方实现该回调方法处理逻辑
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void callback(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("-----------客户端已经收到消息并成功消费-------------");
        } else {
            log.error("-----------客户端消费失败-------------");
        }
    }
}
