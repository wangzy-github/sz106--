package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @PackageName: com.itheima.health.controller
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/12
 * @Time: 20:49
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    private static final Logger log = LoggerFactory.getLogger(ValidateCodeController.class);
    /**
     * redis连接池
     */
    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约验证码
     *
     * @param telephone
     * @return
     */
    @GetMapping("/send4Order")
    public Result send4Order(String telephone) {
        //查看是否存在验证码,存在说明已发送过,请注意查收
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_ORDER + telephone;
        if (jedis.exists(key)) {
            jedis.close();
            return new Result(false, "验证码已发送,请注意查收");
        }
        //获取6位验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        log.debug("==========生成的验证码:" + validateCode);
        /*//  - 调用SMSUtils发送验证码
        try {
           SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode+"");
        } catch (ClientException e) {
           log.error("发送验证码失败: {}:{}",telephone,validateCode);
           return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }*/
        //  - 存入redis，设置有效期
        jedis.setex(key,10*60,validateCode+"");
        jedis.close();
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 快速登录验证码
     *
     * @param telephone
     * @return
     */
    @GetMapping("/send4Login")
    public Result send4Login(String telephone){
        //查看是否存在验证码,存在说明已发送过,请注意查收
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_LOGIN + telephone;
        if (jedis.exists(key)) {
            jedis.close();
            return new Result(false, "验证码已发送,请注意查收");
        }
        //获取6位验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        log.debug("==========生成的验证码:" + validateCode);
        /*//  - 调用SMSUtils发送验证码
        try {
           SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode+"");
        } catch (ClientException e) {
           log.error("发送验证码失败: {}:{}",telephone,validateCode);
           return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }*/
        //  - 存入redis，设置有效期
        jedis.setex(key,10*60,validateCode+"");
        jedis.close();
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
