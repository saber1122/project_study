package com.itheima.predicate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author ZSLONG
 * @Description 自定义路由断言
 * @Date 2021/8/10 22:03
 */
@Slf4j
@Component
public class TokenRoutePredicateFactory extends AbstractRoutePredicateFactory<TokenRoutePredicateFactory.Config> {
    /**
     * @Description 无参构造
     * @Author ZhangShuangLong
     * @Date 2021/8/10 22:13
     **/
    public TokenRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public ShortcutType shortcutType() {
        return super.shortcutType();
    }


    /**
     * @return java.util.List<java.lang.String>
     * @Description 2. 加载完配置之后，从配置文件中拿到对应的值
     * @Author ZhangShuangLong
     * @Date 2021/8/10 22:08
     **/
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("headerName");
    }

    /**
     * @param config
     * @return java.util.function.Predicate<org.springframework.web.server.ServerWebExchange>
     * @Description 3. 是否符合路由规则
     * @Author ZhangShuangLong
     * @Date 2021/8/10 22:11
     **/
    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            // 打印配置文件参数值
            String headerName = config.getHeaderName();
            //获取请求路径中的值
            HttpHeaders headers = exchange.getRequest().getHeaders();
            //进行判断
            List<String> header = headers.get(headerName);
            log.info("Token Predicate headers:{}", header);
            // 断言返回的是boolean值
            return header != null && header.size() > 0;
        };
    }


    /**
     * @Description 1. 自定义config类，为了能够拿到配置的规则key
     * @Author ZhangShuangLong
     * @Date 2021/8/10 22:05
     **/
    @Data
    protected static class Config {
        //存储从配置文件中加载的配置
        private String headerName;
    }
}
