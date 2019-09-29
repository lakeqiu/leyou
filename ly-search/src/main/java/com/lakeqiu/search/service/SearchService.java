package com.lakeqiu.search.service;


import com.lakeqiu.common.vo.PageResult;
import com.lakeqiu.item.pojo.*;
import com.lakeqiu.search.pojo.Goods;
import com.lakeqiu.search.pojo.SearchRequest;

/**
 * 构建goods对象并返回（构建索引库）
 * @author lakeqiu
 */
public interface SearchService {

    /**
     * 构建goods
     * @param spu
     * @return
     */
    Goods buildGoods(Spu spu);

    /**
     * 搜索功能
     * @param request
     * @return
     */
    PageResult<Goods> search(SearchRequest request);

    /**
     * 监听商品的增加与修改
     * 根据消息，更新索引
     * @param spuId 商品id
     */
    void createOrUpdateIndex(Long spuId);

    /**
     * 监听商品的删除
     * 根据消息，删除索引
     * @param spuId 商品id
     */
    void deleteIndex(Long spuId);
}
