package com.itheima.driver.feign;

import com.itheima.driver.model.Driver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author ZSLONG
 * @Description 远程调用接口，该接口映射driver服务
 * @Date 2021/8/17 22:08
 */
//取注册中心的名字
@FeignClient("hailtaxi-driver")
public interface DriverFeign {

    /**
     * @param id
     * @param status
     * @return com.itheima.driver.model.Driver
     * @Description 要与driver对应的路径对应
     * @Author ZhangShuangLong
     * @Date 2021/8/17 22:10
     **/
    @PutMapping(value = "/driver/status/{id}/{status}")
    public Driver status(@PathVariable(value = "id") String id, @PathVariable(value = "status") Integer status);
}
