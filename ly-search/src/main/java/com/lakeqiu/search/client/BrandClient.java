package com.lakeqiu.search.client;

import com.lakeqiu.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lakeqiu
 */
@FeignClient(value = "item-service")
public interface BrandClient extends BrandApi {
}
