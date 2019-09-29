package com.lakeqiu.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 品牌实体类
 * @author lakeqiu
 */
@Data
@Table(name = "tb_brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 品牌名称
     */
    private String name;
    /**
     * 品牌图片
     */
    private String image;
    private Character letter;
}
