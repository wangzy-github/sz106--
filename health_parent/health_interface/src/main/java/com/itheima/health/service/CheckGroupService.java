package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/6
 * @Time: 17:42
 */
public interface CheckGroupService {
    /**
     * 新增检查组
     *
     * @param checkGroup
     * @param checkItemIds
     */
    void add(CheckGroup checkGroup, Integer[] checkItemIds);

    /**
     * 分页条件查询
     *
     * @param queryPageBean
     * @return
     */
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查询检查组
     *
     * @param id
     * @return
     */
    CheckGroup findById(int id);

    /**
     * 根据检查组id查询检查项
     *
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    /**
     * 编辑检查组
     *
     * @param checkGroup
     * @param checkItemIds
     */
    void edit(CheckGroup checkGroup, Integer[] checkItemIds);

    /**
     * 根据id删除检查组
     *
     * @param id
     */
    void deleteById (int id) throws HealthException;

    /**
     * 查询所有检查组
     *
     * @return
     */
    List<CheckGroup> findAll();

}
