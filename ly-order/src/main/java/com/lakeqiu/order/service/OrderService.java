package com.lakeqiu.order.service;

import com.lakeqiu.order.dto.OrderDto;

/**
 * @author lakeqiu
 */
public interface OrderService {

    /**
     * 根据订单交流对象生成订单
     * @param orderDto 订单交流对象
     * @return
     */
    Long createOrder(OrderDto orderDto);
}
