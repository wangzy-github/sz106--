package com.itheima.health;

import com.itheima.health.dao.CheckItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @PackageName: com.itheima.health
 * @Another: 王梓因
 * @Version: 1.0
 * @Date: 2021/1/5
 * @Time: 10:51
 */
public class ServiceApplication {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:spring-service.xml");
        System.in.read();
    }
}
