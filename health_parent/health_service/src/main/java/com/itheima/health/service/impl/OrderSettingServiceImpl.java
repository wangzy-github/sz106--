package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @PackageName: com.itheima.health.service.impl
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/9
 * @Time: 18:21
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> orderSettingList) throws HealthException {
        for (OrderSetting orderSetting : orderSettingList) {
            editNumberByDate(orderSetting);
        }
    }

    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
        return orderSettingDao.getOrderSettingByMonth(month + "%");
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) throws HealthException {
        // 判断是否已存在当天的预约记录
        Date orderDate = orderSetting.getOrderDate();
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderDate);
        if (osInDB != null) {
            // 如果存在,则进行预约人数的更新
            if (orderSetting.getNumber() < osInDB.getReservations()) {
                throw new HealthException(POIUtils.sdf.format(orderSetting.getOrderDate()) + "最大预约数量不可小于已预约数量");
            }
            orderSettingDao.update(orderSetting);
            System.out.println(POIUtils.sdf.format(orderSetting.getOrderDate())+"更新了预约");
        } else {
            // 如果不存在,则进行添加
            orderSettingDao.add(orderSetting);
        }
    }
}
