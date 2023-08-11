package com.cattle.lihao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.cattle.lihao.mapper")
@PropertySource("classpath:log4j.properties")
@EnableTransactionManagement
public class CattleLihaoApplication {
    public static void main(String[] args) {
        SpringApplication.run(CattleLihaoApplication.class, args);
    }
}
