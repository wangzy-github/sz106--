package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @PackageName: com.itheima.health.service.impl
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/20
 * @Time: 15:38
 */
@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    /**
     * 根据用户名获取菜单
     *
     * @param username
     * @return
     */
    @Override
    public List<Menu> getMenuByUser(String username) {
        List<Menu> menus = menuDao.getMenuByUser(username, 1,"%");
        if (!CollectionUtils.isEmpty(menus)) {
            for (Menu menu : menus) {
                String path = menu.getPath();
                menu.setChildren(menuDao.getMenuByUser(username, 2,"/"+path+"-%"));
            }
        }
        return menus;
    }

    /**
     * 分页条件查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Menu> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否有分页查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        Page<Menu> page = menuDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<>(page.getTotal(), page.getResult());
    }
}
