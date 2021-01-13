package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * 生成静态页面存放的目录
     */
    @Value("${out_put_path}")
    private String out_put_path;

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
     * 定时任务
     * 获取redis中的记录,生成或删除静态页面
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 18000000)
    public void doGenerateHtml() {
        //获取redis连接对象
        Jedis jedis = jedisPool.getResource();
        //获取redis中的记录
        String key = "setmeal:static:html";
        Set<String> smembers = jedis.zrange(key,0,-1);
        if (smembers != null && smembers.size() > 0) {
            for (String smember : smembers) {
                String[] strings = smember.split("\\|");
                Integer setmealId = Integer.valueOf(strings[0]);
                String operation = strings[1];
                if ("1".equals(operation)) {
                    // 查询套餐详情
                    Setmeal setmealDetail = setmealService.findDetailById(setmealId);
                    setmealDetail.setImg(QiNiuUtils.DOMAIN + setmealDetail.getImg());
                    // 生成套餐详情静态页面
                    log.debug("生成" + setmealDetail.getName() + "的套餐详情页面");
                    generateSetmealDetailHtml(setmealDetail);
                } else {
                    // 删除套餐详情静态页面
                    log.debug("删除套餐详情页面" + setmealId);
                    removeSetmealDetailHtml(setmealId);
                }
                // 删除redis中的记录
                jedis.zrem(key, smember);
            }
            // 重新生成套餐列表页面
            log.debug("重新生成套餐列表页面");
            generateSetmealHtml();
        }
        jedis.close();
    }

    /**
     * 生成套餐列表页面
     */
    private void generateSetmealHtml() {
        // 查询套餐列表
        List<Setmeal> setmealList = setmealService.findAll();
        // 补全图片路径
        setmealList.forEach(setmeal -> {
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        });
        // 构建数据模型
        Map<String, Object> dataMap = new HashMap<>(1);
        dataMap.put("setmealList", setmealList);
        // 生成的文件全路径
        String fileName = out_put_path + "mobile_setmeal.html";
        // 生成文件
        generateFile("mobile_setmeal.ftl", dataMap, fileName);
    }

    /**
     * 删除套餐详情页面
     *
     * @param id
     */
    private void removeSetmealDetailHtml(Integer id) {
        File file = new File(out_put_path,String.format("setmeal_%d.html",id));
        if(file.exists()){
            // 如果文件存在，则删除
            file.delete();
        }
    }

    /**
     * 生成套餐详情页面
     *
     * @param setmeal
     */
    private void generateSetmealDetailHtml(Setmeal setmeal) {
        // 构建数据模型
        Map<String, Object> dataMap = new HashMap<>(1);
        dataMap.put("setmeal", setmeal);
        // 生成的文件全路径
        String fileName = out_put_path + "setmeal_"+setmeal.getId()+".html";
        // 生成文件
        generateFile("mobile_setmeal_detail.ftl", dataMap, fileName);
    }

    /**
     * 生成文件
     *
     * @param templateName
     * @param dataMap
     * @param fileName
     */
    private void generateFile(String templateName, Map<String, Object> dataMap, String fileName) {
        try {
            // 获取模板
            Template template = configuration.getTemplate(templateName);
            // utf-8 不能少了。少了就中文乱码
            BufferedWriter writer =  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),"utf-8"));
            // 填充数据到模板
            template.process(dataMap,writer);
            // 关闭流
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.error("生成静态页面失败",e);
        }
    }

}
