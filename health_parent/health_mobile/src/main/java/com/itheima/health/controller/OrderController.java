package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @PackageName: com.itheima.health.controller
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/13
 * @Time: 16:25
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    /**
     * redis连接池
     */
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;
    /**
     * 提交体检预约信息
     *
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String, String> orderInfo) {
        // 获取redis中的验证码
        Jedis jedis = jedisPool.getResource();
        String telephone = orderInfo.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_ORDER + telephone;
        String codeInRedis = jedis.get(key);
        log.info("codeInRedis:{}", codeInRedis);
        // 判断验证码是否失效
        if (StringUtils.isEmpty(codeInRedis)) {
            jedis.close();
            return new Result(false, "验证码无效,请重新获取验证码");
        }
        // 获取前端输入的验证码
        String codeInUI = orderInfo.get("validateCode");
        log.info("codeInUI:{}", codeInUI);
        // 校验验证码
        if (!codeInRedis.equals(codeInUI)) {
            jedis.close();
            return new Result(false, "验证码输入错误");
        }
        // 验证码校验通过,删除key
        jedis.del(key);
        // 进行预约
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
        Order order = orderService.submitOrder(orderInfo);
        jedis.close();
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }
}
