package com.lakeqiu.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lakeqiu
 */
@Data
@ConfigurationProperties(prefix = "ly.worker")
public class IdWorkerProperties {

    /**
     * 当前机器id
     */
    private long workerId;

    /**
     * 序列号
     */
    private long dataCenterId;
}
