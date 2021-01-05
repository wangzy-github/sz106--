package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @PackageName: com.itheima.health.controller
 * @Another: 王梓因
 * @Version: 1.0
 * @Date: 2021/1/5
 * @Time: 16:24
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    @GetMapping("/findAll")
    public Result findAll() {
        Result result = new Result(true, "查询检查项成功!");
        try {
            List<CheckItem> list = checkItemService.findAll();
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setMessage("查询检查项失败!");
        }
        return result;
    }

    @PostMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        Result result = new Result(true, "添加检查项成功!");
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setMessage("添加检查项失败!");
        }
        return result;
    }
}
