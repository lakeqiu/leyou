package com.lakeqiu.item.service.impl;

import com.lakeqiu.common.enums.ExpectionEnum;
import com.lakeqiu.common.exception.LyException;
import com.lakeqiu.item.mapper.SpecParamMapper;
import com.lakeqiu.item.mapper.SpecGroupMapper;
import com.lakeqiu.item.pojo.SpecGroup;
import com.lakeqiu.item.pojo.SpecParam;
import com.lakeqiu.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lakeqiu
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    @Override
    public List<SpecGroup> querySpecificationByCategoryId(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(specGroup);
        // 查询不到，返回异常信息
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExpectionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }

    @Override
    public void saveSpecification(SpecGroup specGroup) {
        int count = specGroupMapper.insert(specGroup);
        // 插入查询规格组失败
        if (count != 1){
            throw new LyException(ExpectionEnum.SAVE_SPEC_GROUP_ERROR);
        }
    }

    @Override
    public void updateSpecification(SpecGroup specGroup) {
        int count = specGroupMapper.updateByPrimaryKey(specGroup);
        if (count != 1){
            throw new LyException(ExpectionEnum.ALTER_SPEC_GROUP_ERROR);
        }
    }

    @Override
    public void deleteSpecificationById(Long id) {
        int count = specGroupMapper.deleteByPrimaryKey(id);
        if (count != 1){
            throw new LyException(ExpectionEnum.ALTER_SPEC_GROUP_ERROR);
        }
    }

    @Override
    public List<SpecParam> queryParam(Long gid, Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        List<SpecParam> list = specParamMapper.select(specParam);
        // 查询不到
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExpectionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return list;
    }

    @Override
    public void saveParam(SpecParam specParam) {
        int count = specParamMapper.insert(specParam);
        // 新增失败
        if (count != 1){
            throw new LyException(ExpectionEnum.SAVE_ERROR);
        }
    }

    @Override
    public void updateParam(SpecParam specParam) {
        int count = specParamMapper.updateByPrimaryKey(specParam);
        // 更新失败
        if (count != 1){
            throw new LyException(ExpectionEnum.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteParam(Long id) {
        int count = specParamMapper.deleteByPrimaryKey(id);
        // 删除失败
        if (count != 1){
            throw new LyException(ExpectionEnum.DELETE_ERROR);
        }
    }


    @Override
    public List<SpecGroup> queryListByCid(Long cid) {
        // 查询规格组
        List<SpecGroup> specGroups = querySpecificationByCategoryId(cid);
        // 查询当前分类下的参数
        List<SpecParam> specParams = queryParam(null, cid, null);

        // 先把规格参数变为map，key为组id，value是组下所有参数
        Map<Long, List<SpecParam>> map = new HashMap<>();
        for (SpecParam specParam : specParams) {
            if (!map.containsKey(specParam.getGroupId())){
                // 这个规格组id在map中不存在，新增一个
                map.put(specParam.getGroupId(), new ArrayList<>());
            }
            // 将对应规格组id的值放入map中
            map.get(specParam.getGroupId()).add(specParam);
        }
        // 填充List<SpecParam>到specGroup中
        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(map.get(specGroup.getId()));
        }
        return specGroups;
    }
}
