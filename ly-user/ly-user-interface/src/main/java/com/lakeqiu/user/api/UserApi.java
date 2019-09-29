package com.lakeqiu.user.api;

import com.lakeqiu.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 对其他微服务提供的服务
 * @author lakeqiu
 */
public interface UserApi {
    /**
     * 根据用户名与密码查询用户信息
     * @param username 用户名
     * @param password 用户密码
     * @return 用户信息类
     */
    @GetMapping("query")
    User queryUserByUsernameAndPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password);
}
