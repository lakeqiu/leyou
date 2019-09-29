package com.lakeqiu.search.client;

import com.lakeqiu.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 通过Feign调用item-service的一些借口，
 * 要调用的接口写在下面，方法名，参数与item-service的要一样
 * @author lakeqiu
 */
@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryApi {

}
