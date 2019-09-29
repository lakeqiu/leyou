package com.lakeqiu.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author lakeqiu
 */
@Data
@ConfigurationProperties(prefix = "ly.upload")
public class UploadProperties {
    private String baseUrl;
    private List<String> allowTypes;
}

