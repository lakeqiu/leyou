package com.lakeqiu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 搜索微服务
 * @author lakeqiu
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients // 可以调用其他微服务的api
public class LySearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(LySearchApplication.class, args);
    }
}
