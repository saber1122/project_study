package com.itheima.pay.mq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

/***
 * 消息发送【消息生产者】
 */
@EnableBinding(Source.class)
public class MessageSender {

    //发送消息的对象
    @Resource
    private MessageChannel output;

    /***
     * 发消息
     */
    public Boolean send(Object message){
        return output.send((Message<Object>) MessageBuilder.withPayload(message).build());
    }
}
