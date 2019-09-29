package com.lakeqiu.user.service;

import com.lakeqiu.user.pojo.User;

/**
 * @author lakeqiu
 */
public interface UserService {
    /**
     * 校验数据是否可用（用户名，手机号）
     * @param data 数据
     * @param type 数据类型
     * @return 布尔值
     */
    Boolean checkData(String data, Integer type);

    /**
     * 发送验证码
     * @param phone 要发送的手机号
     */
    void sendCode(String phone);


    /**
     * 用户注册
     * @param user 用户信息
     * @param code 验证码
     */
    void register(User user, String code);

    /**
     * 根据用户名与密码查询用户信息
     * @param username 用户名
     * @param password 用户密码
     * @return 用户信息类
     */
    User queryUserByUsernameAndPassword(String username, String password);
}
