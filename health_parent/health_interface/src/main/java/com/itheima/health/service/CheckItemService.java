package com.itheima.health.service;

import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * @Another: 王梓因
 * @Version: 1.0
 * @Date: 2021/1/5
 * @Time: 16:17
 */
public interface CheckItemService {
    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 添加检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);
}
