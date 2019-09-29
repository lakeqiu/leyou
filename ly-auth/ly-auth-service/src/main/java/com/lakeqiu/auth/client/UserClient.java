package com.lakeqiu.auth.client;

import com.lakeqiu.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 使能够调用user-service提供的接口
 * @author lakeqiu
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
