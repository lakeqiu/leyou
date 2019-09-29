package com.lakeqiu.auth.web;

import com.lakeqiu.auth.config.JwtProperties;
import com.lakeqiu.auth.entity.UserInfo;
import com.lakeqiu.auth.service.AuthService;
import com.lakeqiu.auth.utils.JwtUtils;
import com.lakeqiu.common.enums.ExpectionEnum;
import com.lakeqiu.common.exception.LyException;
import com.lakeqiu.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lakeqiu
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties properties;

    /**
     * 属性注入
     */
    @Value("${ly.jwt.cookieName}")
    private String cookieName;

    /**
     * 登录授权中心，并将token写入cookie
     * @param username 用户账号
     * @param password 用户密码
     * @param response
     * @param request
     * @return 无
     */
    @PostMapping("login")
    public ResponseEntity<Void> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletResponse response,
            HttpServletRequest request
    ){
        // 登录
        String token = authService.login(username, password);

        // 将token写入token,httpOnly()为true不允许js代码操作cookie，request不允许别的域发送此cookie
        CookieUtils.newBuilder(response).httpOnly().request(request)
                .build(cookieName, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 校验用户登录状态
     * @param token 用户凭证
     * @return 用户信息
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN") String token,
                                           HttpServletResponse response,
                                           HttpServletRequest request){
        // 此处没必要判断token是否为空，因为当token为空时，解析也会失败
        try {
            // 解析token
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, properties.getPublicKey());

            // token的有效时间是30分钟，但是用户如果这30分钟内有操作
            // 那么token就不应该失效，故每次用户操作一次，就刷新一次token
            // 刷新token
            String newToken = JwtUtils.generateToken(userInfo, properties.getPrivateKey(), properties.getExpire());
            // 写入cookie
            CookieUtils.newBuilder(response).httpOnly().request(request)
                    .build(cookieName, newToken);

            // 已登录，返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            // token为空，过期，或被篡改
            throw new LyException(ExpectionEnum.USER_PASSWORD_ERROR);
        }
    }

}
