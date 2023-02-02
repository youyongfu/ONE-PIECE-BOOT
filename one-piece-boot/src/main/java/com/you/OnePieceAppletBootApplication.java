package com.you;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.you.mapper")   // 扫描mapper接口包，不用在接口上加@Mapper注解
@SpringBootApplication
public class OnePieceAppletBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnePieceAppletBootApplication.class, args);
    }
    
    
}
