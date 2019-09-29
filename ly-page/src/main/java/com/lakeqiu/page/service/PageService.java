package com.lakeqiu.page.service;

import java.util.Map;

/**
 * @author lakeqiu
 */
public interface PageService {
    /**
     * 获取商品详情
     * @param spuId
     * @return
     */
    Map<String, Object> loadModel(Long spuId);

    /**
     * 根据商品id生成对应静态模板
     * @param spuId 商品id
     */
    void createHtml(Long spuId);

    /**
     * 根据商品id删除对应静态模板
     * @param spuId 商品id
     */
    void deleteHtml(Long spuId);
}
