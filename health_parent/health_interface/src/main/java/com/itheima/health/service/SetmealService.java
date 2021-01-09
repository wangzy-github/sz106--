package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;

import java.util.List;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/8
 * @Time: 16:48
 */
public interface SetmealService {
    /**
     * 新增套餐
     *
     * @param setmeal
     * @param checkGroupIds
     */
    void add(Setmeal setmeal, Integer[] checkGroupIds);

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    Setmeal findById(int id);

    /**
     * 根据id查询套餐中的检查组
     *
     * @param id
     * @return
     */
    List<Integer> findCheckGroupIdsBySetmealId(int id);

    /**
     * 编辑套餐
     *
     * @param setmeal
     * @param checkGroupIds
     */
    void edit(Setmeal setmeal, Integer[] checkGroupIds);

    /**
     * 分页条件查询
     *
     * @param queryPageBean
     * @return
     */
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id删除套餐
     *
     * @param id
     */
    void deleteById(int id) throws HealthException;

    /**
     * 查询所有图片
     *
     * @return
     */
    List<String> findImgs();
}