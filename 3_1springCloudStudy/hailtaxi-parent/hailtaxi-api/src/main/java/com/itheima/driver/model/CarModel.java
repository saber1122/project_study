package com.itheima.driver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/***
 * 车型
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CarModel  implements Serializable {

    //车型  0  快车   1   专车  2  出租车   3  六座商务车
    private Integer carNo;
    //车型描述
    private String name;

    public CarModel(Integer carNo, String name) {
        this.carNo = carNo;
        this.name = name;
    }
}
