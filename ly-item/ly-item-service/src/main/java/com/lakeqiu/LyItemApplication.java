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
@MapperScan("com.lakeqiu.item.mapper")
public class LyItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyItemApplication.class, args);
    }
}
