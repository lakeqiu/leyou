package com.lakeqiu.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 过滤白名单配置类
 * @author lakeqiu
 */
@Data
@ConfigurationProperties(prefix = "ly.filter")
public class FilterProperties {
    private List<String> allowPaths;
}
