package com.lakeqiu.order.interceptor;

import com.lakeqiu.auth.entity.UserInfo;
import com.lakeqiu.auth.utils.JwtUtils;
import com.lakeqiu.common.utils.CookieUtils;
import com.lakeqiu.order.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限校验登录拦截器
 * @author lakeqiu
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private JwtProperties properties;

    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    public UserInterceptor(JwtProperties properties) {
        this.properties = properties;
    }

    public UserInterceptor() {
    }

    /**
     * 前置拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取cookie中的token
        String token = CookieUtils.getCookieValue(request, properties.getCookieName());
        try {
            // 解析token
            UserInfo user = JwtUtils.getInfoFromToken(token, properties.getPublicKey());
            // 解析token成功，获取了用户信息
            // 传递用户信息（user）
            // springmvc不推荐使用request的任何api（这里用这个不太好），故不使用
            // request.setAttribute("user", user);

            // 因此，我们使用了线程容器来存放用户信息并传递过去,这个线程容器底层使用了map
            // 看起来之所以这样，是因为为了安全系统帮我们主动生成key并隐藏了
            tl.set(user);

            // 放行
            return true;

        }catch (Exception e){
            // 解析token失败，说明token无效
            log.error("[购物车解析用户身份失败]", e);
            // 拦截下来
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空线程容器
        tl.remove();
    }

    /**
     * 给微服务提取线程容器中用户信息的方法
     * @return
     */
    public static UserInfo getUser(){
        return tl.get();
    }
}
