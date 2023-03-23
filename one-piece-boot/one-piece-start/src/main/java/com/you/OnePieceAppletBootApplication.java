package com.you;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.you.*"})  //指定扫描包路径
public class OnePieceAppletBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnePieceAppletBootApplication.class, args);
    }

}
