package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/20
 * @Time: 15:38
 */
public interface MenuDao {
    /**
     * 根据登录的用户名获取用户的菜单
     *
     * @param username
     * @return
     */
    List<Menu> getMenuByUser(@Param("username") String username,@Param("level") int level,@Param("path") String path);

    /**
     * 分页条件查询
     *
     * @param queryString
     * @return
     */
    Page<Menu> findByCondition(String queryString);
}
