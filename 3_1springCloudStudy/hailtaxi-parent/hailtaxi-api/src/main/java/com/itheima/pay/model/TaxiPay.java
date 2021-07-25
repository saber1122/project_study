package com.itheima.pay.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/***
 * 打车支付信息
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaxiPay implements Serializable {
    //ID
    private String id;
    //支付订单ID
    private String outTradeNo;
    //支付金额
    private Integer money;
    //支付状态   0  未支付   1  已支付   2  支付超时   3  支付完成
    private Integer status;
}
