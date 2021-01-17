package com.itheima.health.dao;

import com.itheima.health.pojo.User;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/14
 * @Time: 16:51
 */
public interface UserDao {
    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    User findByUsername(String username);
}
