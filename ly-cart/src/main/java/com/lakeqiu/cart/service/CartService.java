package com.lakeqiu.cart.service;

import com.lakeqiu.cart.poji.Cart;

import java.util.List;

/**
 * @author lakeqiu
 */
public interface CartService {

    /**
     * 将商品添加到购物车中
     * @param cart 商品
     */
    void addCart(Cart cart);

    /**
     * 查询用户购物车里面的商品
     * @return 商品集合
     */
    List<Cart> queryCartList();

    /**
     * 根据商品skuId修改商品数量
     * @param skuId 商品id
     * @param num 数量
     */
    void updateNum(Long skuId, Integer num);

    /**
     * 根据商品id删除购物车里相应商品
     * @param skuId 商品id
     */
    void deleteCart(Long skuId);
}
