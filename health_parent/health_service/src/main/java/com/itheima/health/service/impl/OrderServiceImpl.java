package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @PackageName: com.itheima.health.service.impl
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/13
 * @Time: 16:57
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;

    /**
     * 体检预约
     *
     * @param orderInfo
     * @return
     */
    @Override
    @Transactional
    public Order submitOrder(Map<String, String> orderInfo) {
        // ========查询预约设置,判断选定日期是否可以预约
        String orderDateStr = orderInfo.get("orderDate");
        Date orderDate = null;
        try {
            orderDate = sdf.parse(orderDateStr);
        } catch (ParseException e) {
            throw new HeadlessException("日期格式错误");
        }
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderDate);
        if (osInDB == null) {
            throw new HealthException(orderDateStr + "不支持预约,请选择其他日期");
        }
        if (osInDB.getReservations() >= osInDB.getNumber()) {
            throw new HealthException(orderDateStr + "当天预约人数已满");
        }
        // =========构建订单详情
        Order order = new Order();
        order.setSetmealId(Integer.valueOf(orderInfo.get("setmealId")));
        order.setOrderDate(orderDate);
        order.setOrderType(orderInfo.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);

        // =========查询会员
        String telephone = orderInfo.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member == null) {
            // 手机号不存在,新增会员信息
            member = new Member();
            member.setName(orderInfo.get("name"));
            member.setSex(orderInfo.get("sex"));
            member.setPhoneNumber(telephone);
            member.setPassword(telephone.substring(5));// 设置默认密码
            member.setIdCard(orderInfo.get("idCard"));
            member.setRegTime(orderDate);
            member.setRemark("微信预约自动注册");
            memberDao.add(member);
            order.setMemberId(member.getId());
        } else {
            order.setMemberId(member.getId());
            // =========查询是否重复预约
            List<Order> orderList = orderDao.findByCondition(order);
            if (!CollectionUtils.isEmpty(orderList)) {
                throw new HealthException("请勿重复预约");
            }
        }
        // ========当天预约人数更新
        int count = orderSettingDao.addByDate(orderDate);
        if (count==0) {
            throw new HealthException(orderDateStr + "当天预约人数已满");
        }
        orderDao.add(order);
        return order;
    }
}
