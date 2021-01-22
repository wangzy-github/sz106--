package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;

import java.util.List;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/20
 * @Time: 15:34
 */
public interface MenuService {
    /**
     * 根据登录的用户名获取用户的菜单
     *
     * @param username
     * @return
     */
    List<Menu> getMenuByUser(String username);

    /**
     * 分页条件查询
     *
     * @param queryPageBean
     * @return
     */
    PageResult<Menu> findPage(QueryPageBean queryPageBean);

}
