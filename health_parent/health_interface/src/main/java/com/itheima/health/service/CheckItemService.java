package com.itheima.health.service;

import com.github.pagehelper.Page;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/5
 * @Time: 16:17
 */
public interface CheckItemService {
    /**
     * 查询所有检查项
     *
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 添加检查项
     *
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页条件查询
     *
     * @param queryPageBean
     * @return
     */
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查询检查项
     *
     * @param id
     * @return
     */
    CheckItem findById(int id);

    /**
     * 编辑检查项
     *
     * @param checkItem
     */
    void edit(CheckItem checkItem);

    /**
     * 根据id删除检查项
     * @param id
     */
    void deleteById(int id) throws HealthException;
}
