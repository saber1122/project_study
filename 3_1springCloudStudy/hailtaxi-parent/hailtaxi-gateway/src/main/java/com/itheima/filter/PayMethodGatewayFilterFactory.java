package com.itheima.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/8/12 22:04
 */
@Slf4j
@Component
public class PayMethodGatewayFilterFactory extends AbstractGatewayFilterFactory<PayMethodGatewayFilterFactory.Config> {


    public PayMethodGatewayFilterFactory() {
        super(Config.class);
    }

    /**
     * @param config
     * @return org.springframework.cloud.gateway.filter.GatewayFilter
     * @Description 真正的处理逻辑
     * @Author ZhangShuangLong
     * @Date 2021/8/12 22:08
     **/
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String payMethod = config.getPayMethod();
            String msg = config.getMsg();
            log.info("PayMethodGatewayFilterFactory 加载的信息为：{}---{}", payMethod, msg);
            //将paymeehod 添加到请求头中
            exchange.getRequest().mutate().header("paymethod", payMethod);
            return chain.filter(exchange);
        };
    }

    /**
     * @return org.springframework.cloud.gateway.support.ShortcutConfigurable.ShortcutType
     * @Description 数据获取顺序，DEFAULT 默认
     * @Author ZhangShuangLong
     * @Date 2021/8/12 22:29
     **/
    @Override
    public ShortcutType shortcutType() {
        return ShortcutType.DEFAULT;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("payMethod", "msg");
    }

    /**
     * @Description 定义filter的名称
     * @Author ZhangShuangLong
     * @Date 2021/8/12 22:09
     **/
    @Data
    public static class Config {
        private String payMethod;
        private String msg;
    }
}
