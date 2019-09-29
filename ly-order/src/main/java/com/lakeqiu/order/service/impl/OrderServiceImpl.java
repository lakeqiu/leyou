package com.lakeqiu.order.service.impl;

import com.lakeqiu.auth.entity.UserInfo;
import com.lakeqiu.common.enums.ExpectionEnum;
import com.lakeqiu.common.exception.LyException;
import com.lakeqiu.common.utils.IdWorker;
import com.lakeqiu.item.pojo.Sku;
import com.lakeqiu.order.client.AddressClient;
import com.lakeqiu.order.client.GoodsClient;
import com.lakeqiu.order.dto.AddressDTO;
import com.lakeqiu.order.dto.CartDto;
import com.lakeqiu.order.dto.OrderDto;
import com.lakeqiu.order.interceptor.UserInterceptor;
import com.lakeqiu.order.mapper.OrderDetailMapper;
import com.lakeqiu.order.mapper.OrderMapper;
import com.lakeqiu.order.mapper.OrderStatusMapper;
import com.lakeqiu.order.pojo.Order;
import com.lakeqiu.order.pojo.OrderDetail;
import com.lakeqiu.order.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lakeqiu
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private GoodsClient goodsClient;

    @Override
    public Long createOrder(OrderDto orderDto) {
        // 创建新订单
        Order order = new Order();
        // 1.组织订单，创建订单
        // 1.1 订单编号，基本信息
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDto.getPaymentType());

        // 1.2 用户信息
        UserInfo user = UserInterceptor.getUser();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        order.setBuyerRate(false);

        // 1.3 收货人信息
        // 收货人地址信息，应该从数据库中物流信息中获取，这里使用的是假的数据
        AddressDTO addressDTO = AddressClient.findById(orderDto.getAddressId());
        if (addressDTO == null) {
            // 商品不存在，抛出异常
            throw new LyException(ExpectionEnum.RECEIVER_ADDRESS_NOT_FOUND);
        }
        order.setReceiver(addressDTO.getName());
        order.setReceiverAddress(addressDTO.getAddress());
        order.setReceiverCity(addressDTO.getCity());
        order.setReceiverDistrict(addressDTO.getDistrict());
        order.setReceiverMobile(addressDTO.getPhone());
        order.setReceiverZip(addressDTO.getZipCode());
        order.setReceiverState(addressDTO.getState());

        // 1.4 金额
        // 获取订单商品
        Map<Long, Integer> numMap = orderDto.getCarts().stream()
                .collect(Collectors.toMap(CartDto::getSkuId, CartDto::getNum));
        // 获取商品skuId
        Set<Long> skuIds = numMap.keySet();
        // 根据skuId查询商品金额
        List<Sku> skus = goodsClient.querySkuBySkuIds(new ArrayList<>(skuIds));

        // 准备订单详情
        List<OrderDetail> orderDetails = new ArrayList<>();

        Long totalPay = 0L;
        for (Sku sku : skus) {
            totalPay = sku.getPrice() * numMap.get(sku.getId());

            // 组织商品详情
            OrderDetail detail = new OrderDetail();
            detail.setImage(StringUtils.substringBefore(sku.getImages(), ","));
            detail.setNum(numMap.get(sku.getId()));
            detail.setOrderId(orderId);
            detail.setOwnSpec(sku.getOwnSpec());
            detail.setPrice(sku.getPrice());
            detail.setTitle(sku.getTitle());
            detail.setSkuId(sku.getId());

            orderDetails.add(detail);
        }

        order.setTotalPay(totalPay);
        // 总金额+邮费-优惠
        order.setActualPay(totalPay + order.getPostFee() - 0);

        // 1.5 写入数据库
        orderMapper.insertSelective(order);

        // 2.创建订单详情
        orderDetailMapper.insertList(orderDetails);

        return null;
    }
}
