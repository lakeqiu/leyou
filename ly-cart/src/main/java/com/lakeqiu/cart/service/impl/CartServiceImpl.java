package com.lakeqiu.cart.service.impl;

import com.lakeqiu.auth.entity.UserInfo;
import com.lakeqiu.cart.interceptor.UserInterceptor;
import com.lakeqiu.cart.poji.Cart;
import com.lakeqiu.cart.service.CartService;
import com.lakeqiu.common.enums.ExpectionEnum;
import com.lakeqiu.common.exception.LyException;
import com.lakeqiu.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lakeqiu
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 存入redis作为key的前缀
     */
    private static final String KEY_PREFIX = "cart:uid:";

    @Override
    public void addCart(Cart cart) {
        // 获取登录用户
        UserInfo user = UserInterceptor.getUser();
        // 存入redis中的数据结构Map<用户key, Map<商品key, 购买数量>>
        // 获取redis存储的用户key
        String  key = KEY_PREFIX + user.getId();
        // 获取商品id(字符串类型)
        String hashKey = cart.getSkuId().toString();

        // 从redis中取出购物车,使用这个方法取出后，其会自动绑定key，我们取到的是value部分
        // 故可以看成Map<商品key, 购买数量>
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);

        // 判断商品是否已经存在的购物车中
        if (operations.hasKey(hashKey)){
            // 存在，修改商品数量

            // 从Map获取相关信息,获得json格式
            String json = operations.get(hashKey).toString();
            // 转化为商品类
            Cart cacheCart = JsonUtils.toBean(json, Cart.class);
            // 修改数量
            cacheCart.setNum(cacheCart.getNum() + cart.getNum());

            // 重新写回redis,商品类转化为json格式
            operations.put(hashKey, JsonUtils.toString(cacheCart));
        }else {
            // 购物车中没有，直接写入redis
            operations.put(hashKey, JsonUtils.toString(cart));
        }

    }

    @Override
    public List<Cart> queryCartList() {
        // 获取登录用户信息
        UserInfo user = UserInterceptor.getUser();
        // 获取用户Key
        String key = KEY_PREFIX + user.getId();
        /*// 用户购物车中没有商品
        if (!redisTemplate.hasKey(key)){
            return null;
        }*/

        // 从redis中根据用户Key取出购物车相关信息
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        List<Cart> carts = operations.values().stream()
                .map(o -> JsonUtils.toBean(o.toString(), Cart.class)).collect(Collectors.toList());

        return carts;
    }


    @Override
    public void updateNum(Long skuId, Integer num) {
        // 获取登录用户信息
        UserInfo user = UserInterceptor.getUser();
        // 根据用户key在redis中获取相关购物车
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);

        // 判断是否存在
        if (!operations.hasKey(skuId.toString())){
            // 不存在，抛出异常
            throw new LyException(ExpectionEnum.GOODS_NOT_FOUND);
        }
        // 在购物车中找到相关商品
        String json = operations.get(skuId.toString()).toString();
        Cart cart = JsonUtils.toBean(json, Cart.class);

        // 修改商品数量
        cart.setNum(num);
        // 写回redis
        operations.put(skuId.toString(), JsonUtils.toString(cart));
    }

    @Override
    public void deleteCart(Long skuId) {
        // 获取登录用户信息
        UserInfo user = UserInterceptor.getUser();
        // 用户key
        String key = KEY_PREFIX + user.getId();

        // 删除商品
        redisTemplate.opsForHash().delete(key, skuId.toString());
    }
}
