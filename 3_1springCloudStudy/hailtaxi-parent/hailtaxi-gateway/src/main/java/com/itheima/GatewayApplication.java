package com.itheima;
import com.itheima.filter.PayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.lang.reflect.Method;

/**
 * @author 11863
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }

    /**
     * @Description 构建路由
     * @Author ZhangShuangLong
     * @Date 2021/8/7 13:13
     * @param builder
     * @return  org.springframework.cloud.gateway.route.RouteLocator
     **/
    //@Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
                //来自 hailtaxi-driver（注册中心），path：请求路径  ，uri：要路由的地址（lb 负载均衡）
                .route("hailtaxi-driver",r->r.path("/api/driver/**").and().cookie("username","itheiam")
                        .and().header("token","123456").and().method(HttpMethod.GET,HttpMethod.POST).
                                filters(f->f.filter((GatewayFilter) new PayFilter()).addResponseHeader("AddResponseHeader=X-Response-DefaultMyName","zslong")
                                .stripPrefix(1)).uri("lb://hailtaxi-driver"))
                .route("hailtaxi-order",r->r.path("/order/**").uri("lb://hailtaxi-order"))
                .route("hailtaxi-pay",r->r.path("/pay/**").uri("lb://hailtaxi-pay"))
                .build();
    }
}