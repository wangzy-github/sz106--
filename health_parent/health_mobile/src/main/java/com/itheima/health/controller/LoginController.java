package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @PackageName: com.itheima.health.controller
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/13
 * @Time: 18:34
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    /**
     * 手机快速登录
     *
     * @param loginInfo
     * @return
     */
    @PostMapping("/check")
    public Result check(@RequestBody Map<String, String> loginInfo, HttpServletResponse res) {
        Jedis jedis = jedisPool.getResource();
        String telephone = loginInfo.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_LOGIN + telephone;
        String codeInRedis = jedis.get(key);
        if (StringUtils.isEmpty(codeInRedis)) {
            jedis.close();
            return new Result(false, "验证码无效,请重新发送验证码");
        }
        String codeInUI = loginInfo.get("validateCode");
        if (!codeInRedis.equals(codeInUI)) {
            jedis.close();
            return new Result(false, "验证码无效,请重新发送验证码");
        }
        jedis.del(key);
        jedis.close();
        // 查询手机号是否已注册
        Member member = memberService.findByTelephone(telephone);
        if (member == null) {
            // 注册到会员
            member=new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setRemark("快速登录");
            memberService.add(member);
        }
        // 添加cookie跟踪
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setPath("/");
        res.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
