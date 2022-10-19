package com.capol.component.framework.callback;

import com.capol.component.framework.callback.base.MsgSendReturnCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.stereotype.Component;

/**
 * 交换机通过路由规则将消息分发到指定队列失败时回调
 */
@Slf4j
@Component
public class RMQMsgSendReturnCallBack extends MsgSendReturnCallBack {
    /**
     * 业务方实现该回调方法处理逻辑
     *
     * @param returnedMessage
     */
    @Override
    public void callback(ReturnedMessage returnedMessage) {
        log.error("--------交换机通过路由规则将消息分发到指定队列失败-----------");
    }
}
