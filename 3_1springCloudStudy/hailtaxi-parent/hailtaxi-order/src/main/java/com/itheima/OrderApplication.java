package com.itheima;

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
        SpringApplication.run(OrderApplication.class,args);
    }

    @Bean
    public Logger.Level feignLoggerLevel(){
        //- `Logger.Level.FULL`：显示所有日志
        //- `Logger.Level.BASIC：` 显示基本信息
        //- `Logger.Level.HEADERS：`在基本信息的基础上添加头信息
        //- `Logger.Level.NONE：`不显示，feign默认级别
        return Logger.Level.FULL;
    }
}
