package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @PackageName: com.itheima.health.service.impl
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/15
 * @Time: 21:07
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 获取运营统计数据
     *
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() {
        Map<String, Object> reportData = new HashMap<>();
        // 日期
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 今天
        String reportDate = sdf.format(today);
        // 本周一
        String monday = sdf.format(DateUtils.getFirstDayOfWeek(today));
        // 本周最后一天
        String sunday = sdf.format(DateUtils.getLastDayOfWeek(today));
        // 本月1号
        String firstDay4ThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());
        // 本月最后一天
        String lastDay4ThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());

        //=========会员数据统计
        // 本日新增会员数
        int todayNewMember = memberDao.getNewMemberByDate(reportDate);
        // 总会员数
        int totalMember = memberDao.getTotalMember();
        // 本周新增会员数
        int thisWeekNewMember = memberDao.getNewMemberAfterDate(monday);
        // 本月新增会员数
        int thisMonthNewMember = memberDao.getNewMemberAfterDate(firstDay4ThisMonth);

        //==========预约数据统计
        // 今日预约人数
        int todayOrderNumber = orderDao.getOrderNumberByDate(reportDate);
        // 今日到诊人数
        int todayVisitsNumber = orderDao.getVisitsNumberByDate(reportDate);
        // 本周预约人数
        int thisWeekOrderNumber = orderDao.getOrderNumberBetweenDate(monday, sunday);
        // 本周到诊人数
        int thisWeekVisitsNumber = orderDao.getVisitsNumberBetweenDate(monday,sunday);
        // 本月预约人数
        int thisMonthOrderNumber = orderDao.getOrderNumberBetweenDate(firstDay4ThisMonth, lastDay4ThisMonth);
        // 本月到诊人数
        int thisMonthVisitsNumber = orderDao.getVisitsNumberBetweenDate(firstDay4ThisMonth,lastDay4ThisMonth);

        //=========热门套餐
        List<Map<String,Object>> hotSetmeal = orderDao.getHotSetmeal();
        reportData.put("reportDate",reportDate);
        reportData.put("todayNewMember",todayNewMember);
        reportData.put("totalMember",totalMember);
        reportData.put("thisWeekNewMember",thisWeekNewMember);
        reportData.put("thisMonthNewMember",thisMonthNewMember);
        reportData.put("todayOrderNumber",todayOrderNumber);
        reportData.put("todayVisitsNumber",todayVisitsNumber);
        reportData.put("thisWeekOrderNumber",thisWeekOrderNumber);
        reportData.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        reportData.put("thisMonthOrderNumber",thisMonthOrderNumber);
        reportData.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        reportData.put("hotSetmeal",hotSetmeal);
        return reportData;
    }
}
