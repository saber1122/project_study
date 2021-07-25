package com.itheima.order.mapper;

import com.itheima.order.model.OrderInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoMapper {

    @Insert("insert into order_info(id,money,create_time,start_address,end_address,driver_id) values(#{id},#{money},#{createTime},#{startAddress},#{endAddress},#{driver.id})")
    void add(OrderInfo orderInfo);
}
