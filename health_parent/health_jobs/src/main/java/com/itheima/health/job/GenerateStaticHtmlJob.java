package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.SetmealService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @PackageName: com.itheima.health.job
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/11
 * @Time: 16:16
 */
@Component("generateStaticHtmlJob")
public class GenerateStaticHtmlJob {

    private static final Logger log = LoggerFactory.getLogger(GenerateStaticHtmlJob.class);

    @Autowired
    private Configuration configuration;

    /**
     * jedis连接池
     */
    @Autowired
    private JedisPool jedisPool;

    /**
     * 订阅套餐服务
     */
    @Reference
    private SetmealService setmealService;


    /**
     * spring创建对象后，调用的初始化方法
     */
    @PostConstruct
    private void init() {
        // 设置模板所在
        configuration.setClassForTemplateLoading(GenerateStaticHtmlJob.class, "/ftl");
        // 指定默认编码
        configuration.setDefaultEncoding("utf-8");
    }

    /**
     * 定时获取redis中的记录,生成或删除静态页面
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 18000000)
    public void doGenerateHtml() {
        //获取redis连接对象
        Jedis jedis = jedisPool.getResource();
        String key = "static:setmeal:html";
        Set<String> smembers = jedis.smembers(key);
        if (smembers != null && smembers.size() > 0) {
            for (String smember : smembers) {
                String[] strings = smember.split("\\|");
                String setmealId = strings[0];
                String command = strings[1];
            }
        }
    }
}
