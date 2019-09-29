package com.lakeqiu.item.api;

import com.lakeqiu.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lakeqiu
 */
public interface BrandApi {
    /**
     * 根据id查询商品品牌
     * @param id
     * @return
     */
    @GetMapping("brand/{id}")
    Brand queryBrandById(@PathVariable("id")Long id);

    @GetMapping("brand/list")
    List<Brand> queryBrandByIds(@RequestParam("ids")List<Long> ids);
}
