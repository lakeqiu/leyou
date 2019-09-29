package com.lakeqiu.item.service;

import com.lakeqiu.common.vo.PageResult;
import com.lakeqiu.item.pojo.Brand;
import com.lakeqiu.item.vo.BrandVo;

import java.util.List;

/**
 * 品牌服务接口
 * @author lakeqiu
 */
public interface BrandService {
    /**
     * 分页查询品牌
     * @param page 当前页
     * @param rows 一页显示数量
     * @param sortBy 是否排序（默认false）
     * @param desc 排序方式 false：ASC ； true：DESC
     * @param key 过滤条件
     */
    PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    /**
     * 新增品牌功能
     * @param brand 品牌信息类
     * @param cids 品牌所属分类cid集合
     */
    void saveBrand(Brand brand, List<Long> cids);

    /**
     * 根据id查询品牌
     * @param id 品牌id
     * @return 品牌信息类
     */
    Brand queryById(Long id);

    /**
     * 更新品牌
     * @param brandVo
     * @return
     */
    void updateBrand(BrandVo brandVo);

    /**
     * 删除品牌
     * @param bid
     * @return
     */
    void deleteBrand(Long bid);

    /**
     * 根据cid查询这个分类下所有品牌
     * @param cid 分类cid
     * @return 品牌集合
     */
    List<Brand> queryBrandsByCid(Long cid);

    List<Brand> queryBrandByIds(List<Long> ids);
}
