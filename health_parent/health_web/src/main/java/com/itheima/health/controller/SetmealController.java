package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @PackageName: com.itheima.health.controller
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/8
 * @Time: 16:28
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 上传图片
     * 
     * @param imgFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //- 获取原有图片名称，截取到后缀名
        String originalFilename = imgFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //- 生成唯一文件名，拼接后缀名
        String filename = UUID.randomUUID() + extension;
        //- 调用七牛上传文件
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), filename);
            //- 返回数据给页面
            //{
            //    flag:
            //    message:
            //    data:{
            //        imgName: 图片名,
            //        domain: QiNiuUtils.DOMAIN
            //    }
            //}
            Map<String,String> map = new HashMap<String,String>(2);
            map.put("imgName",filename);
            map.put("domain", QiNiuUtils.DOMAIN);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    /**
     * 新增套餐
     *
     * @param setmeal
     * @param checkGroupIds
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkGroupIds){
        Integer setmealId = setmealService.add(setmeal, checkGroupIds);
        // 添加成功, 生成静态页面
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        Long currentTimeMillis = System.currentTimeMillis();
        // zadd setmeal:static:html 时间戳 套餐id|操作符|时间戳
        // jedis.zadd(key, currentTimeMillis.doubleValue(),setmealId+"|1|" + currentTimeMillis);
        jedis.del("setmealList");
        jedis.close();
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Setmeal setmeal = setmealService.findById(id);
        Map<String,Object> map =new HashMap<>(2);
        map.put("setmeal",setmeal);
        map.put("domain",QiNiuUtils.DOMAIN);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,map);
    }

    /**
     * 根据id查询套餐中的检查组
     *
     * @param id
     * @return
     */
    @GetMapping("/findCheckGroupIdsBySetmealId")
    public Result findCheckGroupIdsBySetmealId(int id){
        List<Integer> checkGroupIds = setmealService.findCheckGroupIdsBySetmealId(id);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupIds);
    }

    /**
     * 编辑套餐
     *
     * @param setmeal
     * @param checkGroupIds
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkGroupIds){
        setmealService.edit(setmeal,checkGroupIds);
        // 修改成功, 生成静态页面
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        Long currentTimeMillis = System.currentTimeMillis();
        // zadd setmeal:static:html 时间戳 套餐id|操作符|时间戳
        // jedis.zadd(key, currentTimeMillis.doubleValue(),setmeal.getId()+"|1|" + currentTimeMillis);
        jedis.del("setmealList", "setmealDetail_" + setmeal.getId());
        jedis.close();
        return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    /**
     * 分页条件查询
     *
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> pageResult=setmealService.findPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }

    /**
     * 根据id删除套餐
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(int id){
        setmealService.deleteById(id);
        // 删除成功, 删除静态页面
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        Long currentTimeMillis = System.currentTimeMillis();
        // zadd setmeal:static:html 时间戳 套餐id|操作符|时间戳
        // jedis.zadd(key, currentTimeMillis.doubleValue(),id+"|0|" + currentTimeMillis);
        jedis.del("setmealList", "setmealDetail_" + id);
        jedis.close();
        return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}
