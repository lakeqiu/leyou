package com.lakeqiu.item.mapper;


import com.lakeqiu.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 分类信息通用mapper接口
 * @author lakeqiu
 */
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {
}
