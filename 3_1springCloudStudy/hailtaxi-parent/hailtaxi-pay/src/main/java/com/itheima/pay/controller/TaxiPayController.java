package com.itheima.pay.controller;

import com.itheima.pay.model.TaxiPay;
import com.itheima.pay.mq.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pay")
public class TaxiPayController {

    /***
     * 对象实例已创建
     *  在SpringIOC容器中
     */
    @Autowired
    private MessageSender messageSender;

    /***
     * 支付  http://localhost:18083/pay/wxpay/1
     * @return
     */
    @GetMapping(value = "/wxpay/{id}")
    public TaxiPay pay(@PathVariable(value = "id")String id){
        //支付操作
        TaxiPay taxiPay = new TaxiPay("No"+(int)(Math.random()*1000000),id,310,3);
        //发送消息
        messageSender.send(taxiPay);
        return taxiPay;
    }

    /***
     * 查询指定订单支付状态
     * @return
     */
    @GetMapping(value = "/status/{id}")
    public TaxiPay status(@PathVariable(value = "id")String id){
        TaxiPay taxiPay = new TaxiPay("No"+(int)(Math.random()*1000000),id,310,1);
        return taxiPay;
    }

}
