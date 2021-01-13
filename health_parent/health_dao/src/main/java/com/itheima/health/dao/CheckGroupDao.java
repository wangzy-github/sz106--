package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/6
 * @Time: 17:43
 */
public interface CheckGroupDao {
    /**
     * 新增检查组
     *
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 添加检查项到检查组
     *
     * @param checkGroupId
     * @param checkItemId
     */
    void addCheckGroupCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkItemId") Integer checkItemId);

    /**
     * 分页条件查询
     *
     * @param queryString
     * @return
     */
    Page<CheckGroup> findByCondition(String queryString);

    /**
     * 根据id查询检查组
     *
     * @param id
     * @return
     */
    CheckGroup findById(int id);

    /**
     * 根据检查组id查询检查项id
     *
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    /**
     * 更新检查组
     *
     * @param checkGroup
     */
    void update(CheckGroup checkGroup);

    /**
     * 删除检查组中的检查项
     *
     * @param checkGroupId
     */
    void deleteCheckGroupCheckItem(Integer checkGroupId);

    /**
     * 根据id删除检查组
     *
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询该检查组id统计套餐
     *
     * @param id
     * @return
     */
    Integer findSetMealCountByCheckGroupId(int id);

    /**
     * 查询所有检查组
     *
     * @return
     */
    List<CheckGroup> findAll();
}
