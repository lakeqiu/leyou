package com.lakeqiu.item.api;

import com.lakeqiu.common.vo.PageResult;
import com.lakeqiu.item.pojo.Sku;
import com.lakeqiu.item.pojo.Spu;
import com.lakeqiu.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lakeqiu
 */
public interface GoodsApi {
    /**
     * 根据spuId查询详情Detail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    SpuDetail queryDetailBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 根据spuId查询下面所有sku
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuid(@RequestParam("id") Long spuId);


    /**
     * 分页查询spu
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key
    );

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);

    /**
     * 根据sku的id集合查询所有sku
     * @param ids
     * @return
     */
    @GetMapping("sku/list/ids")
    List<Sku> querySkuBySkuIds(@RequestParam("ids") List<Long> ids);
}
