package com.itheima.order.service.impl;

import com.itheima.order.mapper.OrderInfoMapper;
import com.itheima.order.model.OrderInfo;
import com.itheima.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itheima.order.service.OrderInfoService;
@Service
public class OrderInfoServiceImpl  implements OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    public void add(OrderInfo orderInfo) {
        orderInfoMapper.add(orderInfo);
    }
}
