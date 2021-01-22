package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PackageName: com.itheima.health.controller
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/15
 * @Time: 16:39
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/getLoginUsername")
    public Result getLoginUsername(Authentication authentication){
        // 获取登录的用户
        org.springframework.security.core.userdetails.User user
                = (org.springframework.security.core.userdetails.User)authentication.getPrincipal();
        // 登陆用户名
        String username = user.getUsername();
        // 返回给前端
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
    }

    @RequestMapping("/loginSuccess")
    public Result loginSuccess(){
        return new Result(true, "登录成功");
    }

    @RequestMapping("/loginFail")
    public Result loginFail(){
        return new Result(false, "用户名或密码错误");
    }
}
