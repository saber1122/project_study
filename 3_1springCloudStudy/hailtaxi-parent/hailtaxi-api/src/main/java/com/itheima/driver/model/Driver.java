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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getStar() {
        return star;
    }

    public void setStar(Float star) {
        this.star = star;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
