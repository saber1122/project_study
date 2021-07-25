package com.itheima.driver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/***
 * 司机信息
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Driver implements Serializable {
    //司机ID
    private String id;
    //司机名字
    private String name;
    //司机好评
    private Float star;
    //绑定车辆信息
    private Car car;
    //状态： 0 未上线，1 在线空闲， 2 接单中  3 接到乘客，行程进行中
    private Integer status;
}
