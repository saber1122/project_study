package com.itheima.order.model;

import com.itheima.driver.model.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;
import java.util.Date;

/***
 * 行程订单
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo  implements Serializable {
    //订单ID
    private String id;
    //订单金额  单位 ： 分
    private Integer money;
    //下单时间
    private Date createTime;
    //订单出发地址
    private String startAddress;
    //订单目的地
    private String endAddress;
    //接单司机
    private Driver driver;
}
