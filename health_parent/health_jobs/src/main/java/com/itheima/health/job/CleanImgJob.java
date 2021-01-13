package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @PackageName: com.itheima.health.job
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/9
 * @Time: 17:05
 */
@Component("cleanImgJob")
public class CleanImgJob {

    private static final Logger log = LoggerFactory.getLogger(CleanImgJob.class);
    @Reference
    private SetmealService setmealService;

    /**
     * 每天凌晨4点清理七牛云上多余的图片
     */
    // @Scheduled(cron = "0 0 4 * * ?")
    public void cleanImg() {
        log.info("开始执行清理图片");
        List<String> imgIn7Niu = QiNiuUtils.listFile();
        log.debug("七牛云上一共有{}张图片", imgIn7Niu.size());
        List<String> imgInDB = setmealService.findImgs();
        log.debug("数据库中一共有{}张图片", imgInDB == null ? 0 : imgInDB.size());
        imgIn7Niu.removeAll(imgInDB);
        if (imgIn7Niu.size()>0) {
            String[] surplusImgs = imgIn7Niu.toArray(new String[]{});
            QiNiuUtils.removeFiles(surplusImgs);
        }else{
            log.debug("没有图片需要清理");
        }
        log.info("图片清理结束");
    }
}
