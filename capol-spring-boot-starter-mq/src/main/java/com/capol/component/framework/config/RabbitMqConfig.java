package com.capol.component.framework.config;


import com.capol.component.framework.callback.base.MsgSendConfirmCallBack;
import com.capol.component.framework.callback.base.MsgSendReturnCallBack;
import com.capol.component.framework.callback.RMQMsgSendConfirmCallBack;
import com.capol.component.framework.properties.RabbitMqProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Autowired
    private RabbitMqProperties rabbitMqProperties;
    @Autowired
    private QueueConfig queueConfig;
    @Autowired
    private ExchangeConfig exchangeConfig;
    @Autowired
    private ConnectionFactory connectionFactory;


    /**
     * 将消息队列1和交换机进行绑定
     */
    @Bean
    public Binding syncBinding() {
        return BindingBuilder.bind(queueConfig.syncQueue()).to(exchangeConfig.directExchange()).with(rabbitMqProperties.getSyncRouting());
    }

    /**
     * 将消息队列2和交换机进行绑定
     */
    @Bean
    public Binding deleteBinding() {
        return BindingBuilder.bind(queueConfig.deleteQueue()).to(exchangeConfig.directExchange()).with(rabbitMqProperties.getDeleteRouting());
    }

    /**
     * queue listener  观察 监听模式
     * 当有消息到达时会通知监听在对应的队列上的监听对象
     *
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer_one() {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        //同时监听多个队列
        simpleMessageListenerContainer.addQueues(queueConfig.syncQueue(), queueConfig.deleteQueue());
        //设置监听外露
        simpleMessageListenerContainer.setExposeListenerChannel(true);
        //设置当前的消费者数量上限
        simpleMessageListenerContainer.setMaxConcurrentConsumers(rabbitMqProperties.getMaxConcurrentConsumers());
        //设置当前的消费者数量
        simpleMessageListenerContainer.setConcurrentConsumers(rabbitMqProperties.getConcurrentConsumers());
        //设置手动签收 --> 这里指定确认方式 Auto为自动  MANUAL为手动
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return simpleMessageListenerContainer;
    }

    /**
     * 定义rabbit template用于数据的接收和发送
     *
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        /**若使用confirm-callback或return-callback，
         * 必须要配置publisherConfirms或publisherReturns为true
         * 每个rabbitTemplate只能有一个confirm-callback和return-callback
         */
        template.setConfirmCallback(msgSendConfirmCallBack());

        /**
         * 使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true，
         * 可针对每次请求的消息去确定’mandatory’的boolean值，
         * 只能在提供’return -callback’时使用，与mandatory互斥
         * ----------------------------------------------------------------
         * 为true时,消息通过交换器无法匹配到队列会返回给生产者 并触发MessageReturn
         * 为false时,匹配不到会直接被丢弃
         */
        template.setMandatory(true);
        template.setReturnCallback(msgSendReturnCallback());
        return template;
    }

    /**
     * 消息确认机制
     * Confirms给客户端一种轻量级的方式，能够跟踪哪些消息被broker处理，
     * 哪些可能因为broker宕掉或者网络失败的情况而重新发布。
     * 确认并且保证消息被送达，提供了两种方式：发布确认和事务。(两者不可同时使用)
     * 在channel为事务时，不可引入确认模式；同样channel为确认模式下，不可使用事务。
     *
     * @return
     */
    @Bean
    public MsgSendConfirmCallBack msgSendConfirmCallBack() {
        return new RMQMsgSendConfirmCallBack();
    }

    /**
     * 发生异常时的消息返回提醒
     *
     * @return
     */
    @Bean
    public MsgSendReturnCallBack msgSendReturnCallback() {
        return new MsgSendReturnCallBack();
    }
}