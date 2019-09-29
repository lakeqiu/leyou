package com.lakeqiu.auth.config;

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
 * @author lakeqiu
 */
@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private int expire;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    /**
     * 对象一旦实例化后，就应该读取公钥和私钥
     * @PostConstruct 会在当前类实例化后执行标注的方法
     */
    @PostConstruct
    public void init() {
        try {
            // 如果公钥和私钥任何一个不存在，先生成
            File pubPath = new File(pubKeyPath);
            File priPath = new File(priKeyPath);
            if (!pubPath.exists() || !priPath.exists()){
                // 生成公钥和私钥
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }

            // 获取公钥和私钥
            publicKey = RsaUtils.getPublicKey(pubKeyPath);
            privateKey = RsaUtils.getPrivateKey(priKeyPath);
        }catch (Exception e){
            logger.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException();
        }

    }
}
