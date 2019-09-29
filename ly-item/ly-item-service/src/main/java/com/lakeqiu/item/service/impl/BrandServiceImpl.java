package com.lakeqiu.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lakeqiu.common.enums.ExpectionEnum;
import com.lakeqiu.common.exception.LyException;
import com.lakeqiu.common.vo.PageResult;
import com.lakeqiu.item.mapper.BrandMapper;
import com.lakeqiu.item.pojo.Brand;
import com.lakeqiu.item.service.BrandService;
import com.lakeqiu.item.vo.BrandVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author lakeqiu
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 分页，使用分页助手
        PageHelper.startPage(page, rows);
        /*
            WHERE 'name'LIKE "%X%" OR letter ==x'
            ORDER BY id DESC
         */
        // 过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)){
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        // 排序
        if (StringUtils.isNotBlank(sortBy)){
            String orderByCause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByCause);
        }

        // 查询
        List<Brand> list = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExpectionEnum.BRAND_NOT_FOUND);
        }

        // 解析分页结果
        PageInfo<Brand> info = new PageInfo<>(list);

        return new PageResult<>(info.getTotal(), list);
    }

//    @Transactional(rollbackFor = LyException.class)
    @Override
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        // 新增品牌
        brand.setId(null);
        // 插入成功会返回1
        int count = brandMapper.insert(brand);
        if (count != 1){
            throw new LyException(ExpectionEnum.SAVE_BRAND_ERROR);
        }
        // 新增中间表
        for (Long cid : cids) {
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if (count != 1){
                throw new LyException(ExpectionEnum.SAVE_BRAND_ERROR);
            }
        }
    }

    @Transactional
    @Override
    public void updateBrand(BrandVo brandVo) {
        Brand brand = new Brand();
        brand.setId(brandVo.getId());
        brand.setName(brandVo.getName());
        brand.setImage(brandVo.getImage());
        brand.setLetter(brandVo.getLetter());

        //更新
        int resultCount = brandMapper.updateByPrimaryKey(brand);
        if (resultCount == 0) {
            throw new LyException(ExpectionEnum.UPDATE_ERROR);
        }
        List<Long> cids = brandVo.getCids();
        //更新品牌分类表


        brandMapper.deleteCategoryBrandByBid(brandVo.getId());

        for (Long cid : cids) {
            resultCount = brandMapper.insertCategoryBrand(cid, brandVo.getId());
            if (resultCount == 0) {
                throw new LyException(ExpectionEnum.UPDATE_ERROR);
            }

        }


    }

    @Transactional
    @Override
    public void deleteBrand(Long bid) {
        int result = brandMapper.deleteByPrimaryKey(bid);
        if (result == 0) {
            throw new LyException(ExpectionEnum.DELETE_ERROR);
        }
        // 删除中间表
        result = brandMapper.deleteCategoryBrandByBid(bid);
        if (result == 0) {
            throw new LyException(ExpectionEnum.DELETE_ERROR);
        }
    }

    @Override
    public Brand queryById(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (null == brand){
            throw new LyException(ExpectionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    @Override
    public List<Brand> queryBrandsByCid(Long cid) {
        return brandMapper.queryByCategoryId(cid);
    }


    @Override
    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> brands = brandMapper.selectByIdList(ids);
        if (null == brands){
            throw new LyException(ExpectionEnum.BRAND_NOT_FOUND);
        }
        return brands;
    }
}
