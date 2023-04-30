package com.example.bujurui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// 开启事务注解的支持，对多张标支持@EnableTransactionManagement

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class BujuruiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BujuruiApplication.class, args);
        log.info("项目启动成功...");
    }
}
