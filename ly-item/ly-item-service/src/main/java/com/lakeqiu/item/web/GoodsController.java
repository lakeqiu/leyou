package com.lakeqiu.item.web;

import com.lakeqiu.common.vo.PageResult;
import com.lakeqiu.item.pojo.Sku;
import com.lakeqiu.item.pojo.Spu;
import com.lakeqiu.item.pojo.SpuDetail;
import com.lakeqiu.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lakeqiu
 */
@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询spu
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key
    ){
        return ResponseEntity.ok(goodsService.querySpuByPage(page, rows, saleable, key));
    }

    /**
     * 新增商品
     * @param spu
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu){
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据spuId查询详情Detail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> queryDetailBySpuId(@PathVariable("spuId") Long spuId){
       return ResponseEntity.ok(goodsService.queryDetailBySpuId(spuId));
    }

    /**
     * 根据spuId查询下面所有sku
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuid(@RequestParam("id") Long spuId){
        return ResponseEntity.ok(goodsService.querySkuBySpuid(spuId));
    }

    /**
     * 根据sku的id集合查询所有sku
     * @param ids
     * @return
     */
    @GetMapping("sku/list/ids")
    public ResponseEntity<List<Sku>> querySkuBySkuIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(goodsService.querySkuBySkuIds(ids));
    }

    /**
     * 修改商品
     * @param spu
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu){
        goodsService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据spu的id查询spu，sku，detail
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        return ResponseEntity.ok(goodsService.querySpuById(id));
    }
}
