package com.lakeqiu.item.service;

import com.lakeqiu.item.pojo.SpecGroup;
import com.lakeqiu.item.pojo.SpecParam;

import java.util.List;

/**
 * @author lakeqiu
 */
public interface SpecificationService {
    /**
     * 根据分类id查询规格组
     * @param cid 分类id
     * @return 规格组集合
     */
    List<SpecGroup> querySpecificationByCategoryId(Long cid);

    /**
     * 保存新增查询规格
     * @param specGroup 查询规格信息
     */
    void saveSpecification(SpecGroup specGroup);

    /**
     * 更改规格信息
     * @param specGroup 规格信息
     */
    void updateSpecification(SpecGroup specGroup);

    /**
     * 删除规格信息
     * @param id 规格信息id
     */
    void deleteSpecificationById(Long id);

    /**
     * 根据组id查询商品规格参数
     * @param gid 组id
     * @param cid
     * @param searching
     * @return 参数集合
     */
    List<SpecParam> queryParam(Long gid, Long cid, Boolean searching);

    /**
     * 新增商品规格参数
     * @param specParam 商品规格参数类
     */
    void saveParam(SpecParam specParam);

    /**
     * 修改商品规格参数
     * @param specParam 商品规格参数类
     */
    void updateParam(SpecParam specParam);

    /**
     * 删除商品规格参数
     * @param id 商品规格参数id
     */
    void deleteParam(Long id);

    /**
     * 查询规格参数组，及组内参数
     * @param cid
     * @return
     */
    List<SpecGroup> queryListByCid(Long cid);
}
