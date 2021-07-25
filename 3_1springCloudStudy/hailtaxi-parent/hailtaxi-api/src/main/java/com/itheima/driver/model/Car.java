package com.itheima.driver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/***
 * 车辆信息
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Car implements Serializable {
    //车辆ID
    private String id;
    //车牌
    private String plateNumber;
    //车品牌
    private String brand;
    //车的颜色
    private String color;
    //状态： 0 空闲，1 繁忙
    private Integer status;
    //车型
    private Integer carModel;
}
