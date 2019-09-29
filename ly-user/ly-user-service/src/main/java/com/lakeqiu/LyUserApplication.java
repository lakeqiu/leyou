package com.lakeqiu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author lakeqiu
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.lakeqiu.user.mapper")
public class LyUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyUserApplication.class, args);
    }
}