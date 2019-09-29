package com.lakeqiu.order.web;

import com.lakeqiu.order.dto.OrderDto;
import com.lakeqiu.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lakeqiu
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 根据订单交流对象生成订单
     * @param orderDto 订单交流对象
     * @return
     */
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody @Valid OrderDto orderDto){
        return ResponseEntity.ok(orderService.createOrder(orderDto));
    }
}
