package com.lakeqiu.page.client;

import com.lakeqiu.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lakeqiu
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {

}
