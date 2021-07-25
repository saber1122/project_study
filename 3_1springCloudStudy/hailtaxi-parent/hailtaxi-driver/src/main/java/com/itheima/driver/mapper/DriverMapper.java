package com.itheima.driver.mapper;

import com.itheima.driver.model.Driver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DriverMapper {

    @Select("select * from driver where id=#{id}")
    Driver findById(String id);

    @Update("update driver set status=#{status} where id=#{id}")
    void update(@Param("id") String id,@Param("status") Integer status);
}
