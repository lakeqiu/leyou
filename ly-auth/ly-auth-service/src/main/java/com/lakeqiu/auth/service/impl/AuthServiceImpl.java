package com.lakeqiu.auth.service.impl;

import com.lakeqiu.auth.client.UserClient;
import com.lakeqiu.auth.config.JwtProperties;
import com.lakeqiu.auth.entity.UserInfo;
import com.lakeqiu.auth.service.AuthService;
import com.lakeqiu.auth.utils.JwtUtils;
import com.lakeqiu.common.enums.ExpectionEnum;
import com.lakeqiu.common.exception.LyException;
import com.lakeqiu.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @author lakeqiu
 */
@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties properties;

    @Override
    public String login(String username, String password) {
        // 校验用户名和密码
        User user = userClient.queryUserByUsernameAndPassword(username, password);

        // 判断
        if (null == user){
            throw new LyException(ExpectionEnum.USER_PASSWORD_ERROR);
        }
       try {
           // 生成token
           String token = JwtUtils.generateToken(new UserInfo(user.getId(), username), properties.getPrivateKey(),
                   properties.getExpire());
           return token;
       }catch (Exception e){
           log.error("[授权中心生成用户token]失败，用户名称{}", username, e);
           throw new LyException(ExpectionEnum.CREATE_TOKEN_ERROR);
       }
    }
}
