package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/8
 * @Time: 17:27
 */
public interface SetmealDao {
    /**
     * 新增套餐
     *
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 添加检查组到套餐
     *
     * @param setmealId
     * @param checkGroupId
     */
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId,@Param("checkGroupId")  Integer checkGroupId);

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    Setmeal findById(int id);

    /**
     * 根据id查询套餐中的检查组id
     *
     * @param id
     * @return
     */
    List<Integer> findCheckGroupIdsBySetmealId(int id);

    /**
     * 更新套餐
     *
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 删除套餐中的检查组
     *
     * @param setmealId
     */
    void deleteSetmealCheckGroup(Integer setmealId);

    /**
     * 分页条件查询
     *
     * @param queryString
     * @return
     */
    Page<Setmeal> findByCondition(String queryString);

    /**
     * 查询使用了该套餐的订单
     *
     * @param id
     * @return
     */
    Integer findOrderCountBySetmealId(int id);

    /**
     * 根据id删除套餐
     *
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询所有图片
     *
     * @return
     */
    List<String> findImgs();

    /**
     * 查询所有套餐
     *
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 根据id获取套餐详情
     *
     * @param id
     * @return
     */
    Setmeal findDetailById(int id);
}
