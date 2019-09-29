package com.lakeqiu.cart.web;

import com.lakeqiu.cart.poji.Cart;
import com.lakeqiu.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lakeqiu
 */
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 将商品添加到购物车中
     * @param cart 商品
     * @return null
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询用户购物车里面的商品
     * @return 商品集合
     */
    @GetMapping("list")
    public ResponseEntity<List<Cart>> queryCartList(){
        return ResponseEntity.ok(cartService.queryCartList());
    }

    /**
     * 根据商品skuId修改商品数量
     * @param skuId 商品id
     * @param num 数量
     * @return null
     */
    @PutMapping
    public ResponseEntity<Void> updateCartNum(@RequestParam("id") Long skuId, @RequestParam("num") Integer num){
        cartService.updateNum(skuId, num);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据商品id删除购物车里相应商品
     * @param skuId 商品id
     * @return null
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") Long skuId){
        cartService.deleteCart(skuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
