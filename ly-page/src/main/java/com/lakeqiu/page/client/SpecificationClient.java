package com.lakeqiu.page.client;

import com.lakeqiu.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lakeqiu
 */
@FeignClient(value = "item-service")
public interface SpecificationClient extends SpecificationApi {
}
