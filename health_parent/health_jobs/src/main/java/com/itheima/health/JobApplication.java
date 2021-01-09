package com.itheima.health;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @PackageName: com.itheima.health
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/9
 * @Time: 17:36
 */
public class JobApplication {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:spring-jobs.xml");
        System.in.read();
    }
}
