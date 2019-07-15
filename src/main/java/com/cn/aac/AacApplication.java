package com.cn.aac;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({ "com.cn.aac.dao" })
public class AacApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AacApplication.class, args);
    }
    
}
