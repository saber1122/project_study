package com.itheima;

import com.itheima.filter.PayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * @author 11863
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * @param builder
     * @return org.springframework.cloud.gateway.route.RouteLocator
     * @Description 构建路由
     * @Author ZhangShuangLong
     * @Date 2021/8/7 13:13
     **/
    //@Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                //来自 hailtaxi-driver（注册中心），path：请求路径  ，uri：要路由的地址（lb 负载均衡）
                .route("hailtaxi-driver", r -> r.path("/api/driver/**").and().cookie("username", "itheiam")
                        .and().header("token", "123456").and().method(HttpMethod.GET, HttpMethod.POST).
                                filters(f -> f.filter((GatewayFilter) new PayFilter()).addResponseHeader("AddResponseHeader=X-Response-DefaultMyName", "zslong")
                                        .stripPrefix(1)).uri("lb://hailtaxi-driver"))
                .route("hailtaxi-order", r -> r.path("/order/**").uri("lb://hailtaxi-order"))
                .route("hailtaxi-pay", r -> r.path("/pay/**").uri("lb://hailtaxi-pay"))
                .build();
    }

    /**
     * @return org.springframework.web.cors.reactive.CorsWebFilter
     * @Description 设置cookie跨域
     * @Author ZhangShuangLong
     * @Date 2021/8/12 23:37
     **/
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // cookie跨域
        //允许携带cookie
        config.setAllowCredentials(Boolean.TRUE);
        config.addAllowedMethod("*");
        //允许所有的源
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        // 配置前端js允许访问的自定义响应头
        config.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource(new PathPatternParser());
        //允许所有请求跨域
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }

    /**
     * @return org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
     * @Description IP限流
     * @Author ZhangShuangLong
     * @Date 2021/8/12 23:38
     **/
    @Bean(name = "ipKeyResolver")
    public KeyResolver userKeyResolver() {
        return new KeyResolver() {
            //exchange 上下文对象
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                //获取远程客户端IP
                String hostName = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
                System.out.println("hostName:" + hostName);
                return Mono.just(hostName);
            }
        };
    }
}