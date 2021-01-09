package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/9
 * @Time: 18:22
 */
public interface OrderSettingDao {
    /**
     * 查询指定日期的预约
     *
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     * 新增一天的预约管理信息
     *
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 更新预约信息
     *
     * @param orderSetting
     */
    void update(OrderSetting orderSetting);
}
