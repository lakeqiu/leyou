package com.lakeqiu.item.service;

import com.lakeqiu.common.vo.PageResult;
import com.lakeqiu.item.pojo.Sku;
import com.lakeqiu.item.pojo.Spu;
import com.lakeqiu.item.pojo.SpuDetail;

import java.util.List;

/**
 * @author lakeqiu
 */
public interface GoodsService {
    PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key);

    /**
     * 新增商品
     * @param spu
     * @return
     */
    void saveGoods(Spu spu);

    /**
     * 根据spuId查询详情Detail
     * @param spuId
     * @return
     */
    SpuDetail queryDetailBySpuId(Long spuId);

    /**
     * 根据spuId查询下面所有sku
     * @param spuId
     * @return
     */
    List<Sku> querySkuBySpuid(Long spuId);

    /**
     * 修改商品
     * @param spu
     * @return
     */
    void updateGoods(Spu spu);

    /**
     * 根据spu的id查询spu，sku，detail
     * @param id
     * @return
     */
    Spu querySpuById(Long id);

    /**
     * 根据sku的id集合查询所有sku
     * @param ids
     * @return
     */
    List<Sku> querySkuBySkuIds(List<Long> ids);
}
