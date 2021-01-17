package com.itheima.health.service;

import com.itheima.health.pojo.Order;

import java.util.Map;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/13
 * @Time: 16:55
 */
public interface OrderService {
    /**
     * 体检预约
     *
     * @param orderInfo
     * @return
     */
    Order submitOrder(Map<String, String> orderInfo);
}
