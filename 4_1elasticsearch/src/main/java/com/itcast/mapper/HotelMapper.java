package com.itcast.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcast.entity.HotelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface HotelMapper extends BaseMapper<HotelEntity> {
}
