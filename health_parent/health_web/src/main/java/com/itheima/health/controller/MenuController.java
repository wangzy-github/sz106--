package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @PackageName: com.itheima.health.controller
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/20
 * @Time: 15:33
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private MenuService menuService;

    /**
     * 获取当前登录用户的菜单
     *
     * @param authentication
     * @return
     */
    @GetMapping("/getMenuList")
    public Result getMenu(Authentication authentication) {
        // 获取登录的用户
        org.springframework.security.core.userdetails.User user
                = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        String username = user.getUsername();
        // 根据用户名获取菜单
        List<Menu> list = menuService.getMenuByUser(username);
        return new Result(true, MessageConstant.GET_MENU_SUCCESS, list);
    }

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<Menu> menus = menuService.findPage(queryPageBean);
        return new Result(true, "获取菜单列表成功", menus);
    }
}
