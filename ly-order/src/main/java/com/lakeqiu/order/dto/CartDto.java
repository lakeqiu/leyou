package com.lakeqiu.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lakeqiu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    /**
     * 商品skuId
     */
    private Long skuId;
    /**
     * 购买数量
     */
    private Integer num;
}
