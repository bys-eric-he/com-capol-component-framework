package com.capol.component.framework.callback;

import com.capol.component.framework.callback.base.MsgSendConfirmCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Component;

/**
 * 生产者到交换机的可靠保证依靠的是： confirmCallback 机制。
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
            log.info("-----------消息已成功到达交换机-------------");
        } else {
            log.error("-----------消息到达交换机失败-------------");
            log.error("---失败原因：{}---", cause);
        }
    }
}
