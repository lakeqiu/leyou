package com.lakeqiu.item.service;

import com.lakeqiu.item.pojo.Category;

import java.util.List;

/**
 * @author lakeqiu
 */
public interface CategoryService {
    /**
     * 查询所有分类
     * @param pid
     * @return
     */
    List<Category> queryCategoryListByPid(Long pid);

    List<Category> queryByIds(List<Long> ids);
}
