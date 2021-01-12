package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @PackageName: com.itheima.health.controller
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/11
 * @Time: 9:51
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    /**
     * 获取套餐列表
     *
     * @return
     */
    @GetMapping("/getSetmeal")
    public Result getSetmeal() {
        List<Setmeal> list = setmealService.findAll();
        list.forEach(setmeal -> {
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        });
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, list);
    }

    /**
     * 获取套餐详情
     *
     * @param id
     * @return
     */
    @GetMapping("/findDetailById")
    public Result findDetailById(int id) {
        Setmeal setmeal = setmealService.findDetailById(id);
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }
}
