package com.lakeqiu.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lakeqiu.common.enums.ExpectionEnum;
import com.lakeqiu.common.exception.LyException;
import com.lakeqiu.common.vo.PageResult;
import com.lakeqiu.item.mapper.SkuMapper;
import com.lakeqiu.item.mapper.SpuDetailMapper;
import com.lakeqiu.item.mapper.SpuMapper;
import com.lakeqiu.item.mapper.StockMapper;
import com.lakeqiu.item.pojo.*;
import com.lakeqiu.item.service.BrandService;
import com.lakeqiu.item.service.CategoryService;
import com.lakeqiu.item.service.GoodsService;
import com.netflix.eureka.Names;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lakeqiu
 */
@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        // 分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        // 搜索字段过滤
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title", "%" + key + "%");
        }
        // 上下架过滤
        if (null != saleable){
            criteria.andEqualTo("saleable", saleable);
        }
        // 默认排序
        example.setOrderByClause("last_update_time DESC");
        // 查询
        List<Spu> spus = spuMapper.selectByExample(example);

        /*// 判断，这个不需要，因为当上架或下架商品为0时不应该报错，而是正常显示
        if (CollectionUtils.isEmpty(spus)){
            throw new LyException(ExpectionEnum.GOODS_NOT_FOUND);
        }*/

        // 解析分类和品牌的名称
        loadCategoryAndBrandName(spus);

        // 解析分页结果
        PageInfo<Spu> info = new PageInfo<>(spus);

        return new PageResult<>(info.getTotal(), spus);
    }

    private void loadCategoryAndBrandName(List<Spu> spus) {
        for (Spu spu : spus) {
            // 处理分类名称
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names, "/"));
            // 处理品牌名称
            Brand brand = brandService.queryById(spu.getBrandId());
            spu.setBname(brand.getName());
        }
    }

//    @Transactional(rollbackFor = LyException.class)
    @Override
    @Transactional
    public void saveGoods(Spu spu) {
        // 新增spu
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);
        int count = spuMapper.insert(spu);
        if (count != 1){
            throw new LyException(ExpectionEnum.SAVE_ERROR);
        }

        // 新增detail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);

        // 新增sku和stock
        saveSkuAndStock(spu);

        // 发送mq消息
//        amqpTemplate.convertAndSend("item.insert", spu.getId());
        sendMessage(spu.getId(), "insert");

    }

    @Override
    public SpuDetail queryDetailBySpuId(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    @Override
    public List<Sku> querySkuBySpuid(Long spuId) {
        // 查询sku
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);

        // 查询库存
        // 查询所有sku的id并放到集合里
        List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        // 根据sku的id查询每个sku的库存
        return loadStockInSku(ids, skuList);
    }

    @Override
    @Transactional
    public void updateGoods(Spu spu) {
        if (null == spu.getId()){
            throw new LyException(ExpectionEnum.GOOD_ID_ERROR);
        }
        // 删除sku和stock
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        // 查询以前的sku
        List<Sku> skuList = skuMapper.select(sku);
        // 如果存在，则删除
        if (!CollectionUtils.isEmpty(skuList)){
            // 删除sku
            skuMapper.delete(sku);
            // 删除stock
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);
        }

        // 修改spu
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);

        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if (count != 1){
            throw new LyException(ExpectionEnum.GOODS_UPDATE_ERROR);
        }

        // 修改detail
        count = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if (count != 1){
            throw new LyException(ExpectionEnum.GOODS_UPDATE_ERROR);
        }

        // 新增sku和stock
        saveSkuAndStock(spu);

        // 发送mq消息
//        amqpTemplate.convertAndSend("item.update", spu.getId());
        sendMessage(spu.getId(), "update");
    }

    private void saveSkuAndStock(Spu spu){
        int count;

        // 定义库存集合
        List<Stock> stockList = new ArrayList<>();
        // 新增sku
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());

            count = skuMapper.insert(sku);
            if (count != 1){
                throw new LyException(ExpectionEnum.SAVE_ERROR);
            }

            // 新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            stockList.add(stock);
            // 用批量新增好
            count = stockMapper.insert(stock);
            if (count != 1){
                throw new LyException(ExpectionEnum.SAVE_ERROR);
            }
        }

         /*// 批量新增,id只能为id，不能为skuid，有两个，一个支持，一个不支持
        stockMapper.insertList(stockList);*/
    }


    @Override
    public Spu querySpuById(Long id) {
        // 查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (null == spu){
            throw new LyException(ExpectionEnum.GOODS_NOT_FOUND);
        }

        // 查询sku
        List<Sku> skuList = querySkuBySpuid(id);
        spu.setSkus(skuList);

        // 查询detail
        SpuDetail spuDetail = queryDetailBySpuId(id);
        spu.setSpuDetail(spuDetail);

        return spu;
    }

    @Override
    public List<Sku> querySkuBySkuIds(List<Long> ids) {
        // 根据ids查询出sku
        List<Sku> skuList = skuMapper.selectByIdList(ids);
        // 判断是否为空
        if (CollectionUtils.isEmpty(skuList)){
            throw new LyException(ExpectionEnum.GOODS_NOT_FOUND);
        }

        // 查询库存并返回
        return loadStockInSku(ids, skuList);
    }

    /**
     * 根据sku的id查询该商品库存
     * @param ids
     * @param skuList
     * @return
     */
    private List<Sku> loadStockInSku(List<Long> ids, List<Sku> skuList) {
        // 查询库存
        // 根据sku的id查询每个sku的库存
        List<Stock> stockList = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(stockList)) {
            throw new LyException(ExpectionEnum.SKU_NOT_FOUND);
        }

        // 把stock变为一个map，key是sku的id，val是库存
        Map<Long, Integer> stockMap = stockList.stream()
                .collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skuList.forEach(s -> s.setStock(stockMap.get(s.getId())));
        return skuList;
    }

    /**
     * 封装发送到消息队列的方法
     *
     * @param id
     * @param type
     */
    private void sendMessage(Long id, String type) {
        try {
            amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            log.error("{}商品消息发送异常，商品ID：{}", type, id, e);
        }
    }
}
