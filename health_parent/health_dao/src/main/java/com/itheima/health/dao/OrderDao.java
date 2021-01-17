package com.itheima.health.dao;

import com.itheima.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/13
 * @Time: 16:59
 */
public interface OrderDao {
    /**
     * 条件查询
     *
     * @param order
     * @return
     */
    List<Order> findByCondition(Order order);

    /**
     * 新增预约详情
     *
     * @param order
     */
    void add(Order order);

    /**
     * 获取指定日期的预约人数
     *
     * @param orderDate
     * @return
     */
    Integer getOrderNumberByDate(String orderDate);

    /**
     * 获取指定日期到诊人数
     *
     * @param orderDate
     * @return
     */
    Integer getVisitsNumberByDate(String orderDate);

    /**
     * 获取指定日期内的预约人数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    Integer getOrderNumberBetweenDate(@Param("startDate") String startDate,@Param("endDate") String endDate);

    /**
     * 获取指定日期内的到诊人数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    Integer getVisitsNumberBetweenDate(@Param("startDate") String startDate,@Param("endDate") String endDate);

    /**
     * 获取热门套餐
     *
     * @return
     */
    List<Map<String, Object>> getHotSetmeal();
}
