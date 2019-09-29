package com.lakeqiu.item.service.impl;

import com.lakeqiu.common.enums.ExpectionEnum;
import com.lakeqiu.common.exception.LyException;
import com.lakeqiu.item.mapper.CategoryMapper;
import com.lakeqiu.item.pojo.Category;
import com.lakeqiu.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lakeqiu
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryCategoryListByPid(Long pid) {
        // 查询条件，mapper会把对象中的非空属性值当成查询条件
        Category category = new Category();
        category.setParentId(pid);
        // 这个方法会根据传入对象的不为空的值去查询
        List<Category> list = categoryMapper.select(category);
        // 查询不到值，返回自定义异常
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExpectionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }

    @Override
    public List<Category> queryByIds(List<Long> ids) {
        List<Category> list = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExpectionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }
}
