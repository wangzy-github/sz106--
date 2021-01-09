package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/5
 * @Time: 16:12
 */
public interface CheckItemDao {
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
     * @param queryString
     * @return
     */
    Page<CheckItem> findByCondition(String queryString);

    /**
     * 根据id查询检查项
     *
     * @param id
     * @return
     */
    CheckItem findById(int id);

    /**
     * 更新检查项
     *
     * @param checkItem
     */
    void update(CheckItem checkItem);

    /**
     * 根据id删除检查项
     *
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询检查项id统计检查组
     *
     * @param id
     */
    Integer findCheckGroupCountByCheckItemId(int id);
}
