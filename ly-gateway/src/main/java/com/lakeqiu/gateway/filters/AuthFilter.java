package com.lakeqiu.gateway.filters;

import com.lakeqiu.auth.entity.UserInfo;
import com.lakeqiu.auth.utils.JwtUtils;
import com.lakeqiu.common.utils.CookieUtils;
import com.lakeqiu.gateway.config.FilterProperties;
import com.lakeqiu.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验过滤器
 * @author lakeqiu
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class AuthFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    /**
     * @return 返回过滤器类型
     */
    @Override
    public String filterType() {
        // 前置过滤
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 过滤器执行顺序
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    /**
     * 除去白名单的都过滤
     * @return 是否过滤
     */
    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();
        // 获取请求路径
        String requestURI = request.getRequestURI();
        // 判断是否放行，放行，返回true
        Boolean flag = isAllowPath(requestURI);

        // 因为要过滤是true，不过滤是false，所以要加"!"
        return !flag;
    }

    /**
     * 判断是否放行请求
     * 即请求路径是否在白名单内
     * @param requestURI 请求路径
     * @return true->放行， false->不放行
     */
    private Boolean isAllowPath(String requestURI) {
        // 遍历放行白名单
        for (String allowPath : filterProperties.getAllowPaths()) {
            if (requestURI.startsWith(allowPath)){
                // 在白名单内，放行
                return true;
            }
        }
        // 不在白名单，不放行
        return false;
    }

    /**
     * 核心校验方法
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();
        // 获取cookie中的token
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        // 解析token
        try {
            UserInfo user = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());

            // 校验权限
            // TODO
        } catch (Exception e) {
            // 解析token失败，未登陆，拦截
            ctx.setSendZuulResponse(false);
            // 返回状态码
            ctx.setResponseStatusCode(403);
        }

        return null;
    }
}
