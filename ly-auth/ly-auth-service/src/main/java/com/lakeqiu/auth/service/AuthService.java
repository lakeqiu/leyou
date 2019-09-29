package com.lakeqiu.auth.service;

/**
 * @author lakeqiu
 */
public interface AuthService {
    /**
     * 登录授权中心
     * @param username
     * @param password
     */
    String login(String username, String password);
}
