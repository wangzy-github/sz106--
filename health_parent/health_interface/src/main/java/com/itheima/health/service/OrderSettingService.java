package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/9
 * @Time: 18:21
 */
public interface OrderSettingService {
    /**
     * 添加预约
     *
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList) throws HealthException;
}
