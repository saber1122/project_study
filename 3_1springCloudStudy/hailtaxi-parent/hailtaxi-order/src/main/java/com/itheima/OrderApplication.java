package com.itheima;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.itheima.driver.feign"})
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        //- `Logger.Level.FULL`：显示所有日志
        //- `Logger.Level.BASIC：` 显示基本信息
        //- `Logger.Level.HEADERS：`在基本信息的基础上添加头信息
        //- `Logger.Level.NONE：`不显示，feign默认级别
        return Logger.Level.FULL;
    }

    /**
     * @return com.netflix.loadbalancer.IRule
     * @Description 替换openFeign默认的轮询算法——将默认的用容器进行替换。负载均衡发生在调用方
     * @Author ZhangShuangLong
     * @Date 2021/9/6 23:33
     **/
    @Bean
    public IRule loadBalanceRule() {
        return new RandomRule();
    }
}
