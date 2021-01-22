package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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

    @Autowired
    private JedisPool jedisPool;

    private static final Logger log = LoggerFactory.getLogger(SetmealController.class);

    /**
     * 获取套餐列表
     *
     * @return
     */
    @GetMapping("/getSetmeal")
    public Result getSetmeal() {
        // 查询redis中是否有套餐列表信息
        Jedis jedis = jedisPool.getResource();
        String setmealListStr = jedis.get("setmealList");
        List<Setmeal> list = null;
        // 如果redis中没有,去数据库查询
        if (StringUtils.isEmpty(setmealListStr)) {
            list = setmealService.findAll();
            list.forEach(setmeal -> {
                setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
            });
            // 将数据库查询的结果存入redis
            setmealListStr = JSON.toJSONString(list);
        }
        jedis.setex("setmealList", 7 * 24 * 60 * 60, setmealListStr);
        jedis.close();
        // 将json字符串转成list集合返回给前端
        list = JSON.parseArray(setmealListStr, Setmeal.class);
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
        // 查询redis中是否有该套餐的详情数据
        Jedis jedis = jedisPool.getResource();
        String key = "setmealDetail_" + id;
        String setmealStr = jedis.get(key);
        Setmeal setmeal = null;
        // redis中没有套餐详情,则去数据库中查询
        if (StringUtils.isEmpty(setmealStr)) {
            setmeal = setmealService.findDetailById(id);
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
            setmealStr = JSON.toJSONString(setmeal);
        }
        // 将数据库中的数据存入redis中,返回给前端页面
        jedis.setex(key, 7 * 24 * 60 * 60, setmealStr);
        jedis.close();
        setmeal = JSON.parseObject(setmealStr, Setmeal.class);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id) {
        Setmeal setmeal = setmealService.findById(id);
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }
}
