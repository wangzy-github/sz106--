package com.itheima.health.service;

import java.util.List;
import java.util.Map;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/15
 * @Time: 21:06
 */
public interface ReportService {
    /**
     * 获取运营统计信息
     *
     * @return
     */
    Map<String, Object> getBusinessReportData();
}
