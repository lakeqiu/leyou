package com.lakeqiu.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lakeqiu
 */
@Data
@ConfigurationProperties(prefix = "ly.sms")
public class SmsProperties {
    String accessKeyId;

    String accessKeySecret;

    String signName;

    String verifyCodeTemplate;
}
