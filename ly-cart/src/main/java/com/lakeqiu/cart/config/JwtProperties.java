package com.lakeqiu.cart.config;

import com.lakeqiu.auth.utils.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 微服务启动时自动获取公钥
 * @author lakeqiu
 */
@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
    private String pubKeyPath;

    private String cookieName;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    /**
     * 对象一旦实例化后，就应该读取公钥和私钥
     * @PostConstruct 会在当前类实例化后执行标注的方法
     */
    @PostConstruct
    public void init() {
        try {
            // 获取公钥和私钥
            publicKey = RsaUtils.getPublicKey(pubKeyPath);

        }catch (Exception e){
            logger.error("获取公钥失败！", e);
            throw new RuntimeException();
        }

    }
}
