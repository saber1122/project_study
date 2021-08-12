package com.itheima.filter;

import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/8/12 21:21
 */
@Component
public class PayFilter implements GlobalFilter, Ordered {
    /**
     * @param exchange
     * @param chain
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @Description 实现过滤的逻辑
     * @Author ZhangShuangLong
     * @Date 2021/8/12 21:24
     **/
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("GatewayFilter拦截器执行---pre--PayFilter");
        return chain.filter(exchange).then(Mono.fromRunnable(()->{
            System.out.println("GatewayFilter拦截器执行--post---PayFilter");
        }));
    }

    /**
     * @return int 返回值越小，过滤的优先级越高
     * @Description 拦截器顺序
     * @Author ZhangShuangLong
     * @Date 2021/8/12 21:23
     **/
    @Override
    public int getOrder() {
        return 0;
    }
}
