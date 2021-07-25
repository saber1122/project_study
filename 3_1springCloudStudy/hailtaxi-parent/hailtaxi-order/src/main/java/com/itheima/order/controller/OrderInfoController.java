package com.itheima.order.controller;

import com.itheima.driver.model.Car;
import com.itheima.driver.model.Driver;
import com.itheima.order.model.OrderInfo;
import com.itheima.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/order")
public class OrderInfoController {

  

    @Autowired
    private OrderInfoService orderInfoService;

    /***
     * 下单
     */
    @PostMapping
    public OrderInfo add(){
        
        //创建订单
        OrderInfo orderInfo = new OrderInfo("No"+((int)(Math.random()*10000)), (int)(Math.random()*100), new Date(), "深圳北站", "罗湖港", null);
        orderInfoService.add(orderInfo);
        return orderInfo;
    }

}
