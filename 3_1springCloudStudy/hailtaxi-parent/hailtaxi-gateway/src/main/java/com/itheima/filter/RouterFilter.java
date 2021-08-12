package com.itheima.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/8/12 22:39
 */
@Slf4j
@Component
public class RouterFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("RouterFilter----------------");
        //获取请求参数
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        //如果token为空，则表示没有登录
        if (StringUtils.isEmpty(token)) {
            //没登录,状态设置403
            exchange.getResponse().setStatusCode(HttpStatus.PAYLOAD_TOO_LARGE);
            //结束请求
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
