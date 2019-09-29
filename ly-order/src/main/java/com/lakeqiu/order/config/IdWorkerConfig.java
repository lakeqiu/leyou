package com.lakeqiu.order.config;

import com.lakeqiu.common.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lakeqiu
 */
@Configuration
@EnableConfigurationProperties(IdWorkerProperties.class)
public class IdWorkerConfig {

    /**
     * 注册IdWorker
     * @param properties
     * @return
     */
    @Bean
    public IdWorker idWorker(IdWorkerProperties properties){
        return new IdWorker(properties.getWorkerId(), properties.getDataCenterId());
    }
}
