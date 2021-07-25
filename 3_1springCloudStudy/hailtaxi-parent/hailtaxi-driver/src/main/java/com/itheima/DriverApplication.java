package com.itheima;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.itheima.driver.mapper")
public class DriverApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverApplication.class,args);
    }
}
