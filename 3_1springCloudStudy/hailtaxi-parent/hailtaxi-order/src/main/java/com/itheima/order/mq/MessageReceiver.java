package com.itheima.order.mq;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@EnableBinding(Sink.class)
public class MessageReceiver {

    @Value("${server.port}")
    private String port;

    /****
     * 消息监听
     * @param message
     */
    @StreamListener(Sink.INPUT)
    public void receive(String message) {
        System.out.println("消息监听(增加用户积分、修改订单状态)-->" + message+"-->port:"+port);
    }
}

