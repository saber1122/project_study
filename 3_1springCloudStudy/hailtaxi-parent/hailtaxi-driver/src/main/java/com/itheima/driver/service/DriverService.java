package com.itheima.driver.service;

import com.itheima.driver.model.Driver;

public interface DriverService {
    Driver findById(String id);

    void update(String id, Integer status);
}
