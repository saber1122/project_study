package com.itheima.driver.controller;

import com.itheima.driver.model.Driver;
import com.itheima.driver.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
@RequestMapping(value = "/driver")
@Slf4j
public class DriverController {

    @Autowired
    private DriverService driverService;

    /****
     * 司机信息
     */
    //@GetMapping(value = "/info/{id}")
    @RequestMapping(value = "/info/{id}")
    public Driver info(@PathVariable(value = "id")String id,HttpServletRequest request){
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name+":"+value);
            System.out.println("--------------------------");
        }
        return driverService.findById(id);
    }

    @Value("${server.port}")
    private int port;

    /****
     * 更新司机信息
     */
    @PutMapping(value = "/status/{id}/{status}")
    public Driver status(@PathVariable(value = "id")String id,@PathVariable(value = "status")Integer status){
        log.info("当前服务占用的端口为:{}",port);
        //修改状态
        driverService.update(id,status);
        //修改状态后的司机信息
        return driverService.findById(id);
    }
}
