package com.lakeqiu.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lakeqiu.common.enums.ExpectionEnum;
import com.lakeqiu.common.exception.LyException;
import com.lakeqiu.common.utils.JsonUtils;
import com.lakeqiu.common.utils.NumberUtils;
import com.lakeqiu.common.vo.PageResult;
import com.lakeqiu.item.pojo.*;
import com.lakeqiu.search.client.BrandClient;
import com.lakeqiu.search.client.CategoryClient;
import com.lakeqiu.search.client.GoodsClient;
import com.lakeqiu.search.client.SpecificationClient;
import com.lakeqiu.search.pojo.Goods;
import com.lakeqiu.search.pojo.SearchRequest;
import com.lakeqiu.search.pojo.SearchResult;
import com.lakeqiu.search.repository.GoodsRepository;
import com.lakeqiu.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 构建goods对象并返回（构建索引库）
 * @author lakeqiu
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Override
    public Goods buildGoods(Spu spu){
        // 查询分类
        List<Category> categories = categoryClient.queryCategoryListByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<String> name = categories.stream().map(Category::getName).collect(Collectors.toList());

        // 查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());

        // 搜索字段
        String all = spu.getTitle() + StringUtils.join(name, " ") + brand.getName();


        // 查询sku
        List<Sku> skuList = goodsClient.querySkuBySpuid(spu.getId());
        // 对sku进行处理，取需要的字段
        List<Map<String, Object>> skus = new ArrayList<>();
        // 价格集合
        Set<Long> priceList = new HashSet<>();
        for (Sku sku : skuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            // 图片有多张，用逗号隔开，只需要一张
            map.put("images", StringUtils.substringBefore(sku.getImages(), ","));
            map.put("price", sku.getPrice());
            skus.add(map);

            // 处理价格
            priceList.add(sku.getPrice());
        }

        // 查询规格参数
        List<SpecParam> params = specificationClient.queryParamByGid(null, spu.getCid3(), true);
        if (CollectionUtils.isEmpty(params)){
            throw new LyException(ExpectionEnum.INDEX_ERROR);
        }
        // 查询商品详情
        SpuDetail spuDetail = goodsClient.queryDetailBySpuId(spu.getId());
        // 获取通用规格参数
        String json = spuDetail.getGenericSpec();
        Map<Long, String> genericSpec = JsonUtils.toMap(json, Long.class, String.class);
        // 获取特有规格参数
        json = spuDetail.getSpecialSpec();
        Map<Long, List<String>> specialSpec = JsonUtils
                .nativeRead(json, new TypeReference<Map<Long, List<String>>>() {});
        // 规格参数，key是规格参数的名字，value是规格参数是值
        Map<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            // 规格名称
            String key = param.getName();
            Object value = "";
            // 判断是否通用规格
            if (param.getGeneric()){
                value = genericSpec.get(param.getId());
                // 判断是否数值类型
                if (param.getNumeric()){
                    // 处理成段
                    value = chooseSegment(value.toString(), param);
                }
            }else {
                value = specialSpec.get(param.getId());
            }
            // 存入map
            specs.put(key, value);
        }


        // 构建Goods对象
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        /**
         * 搜索字段，包含标题，分类，品牌，规格
         */
        goods.setAll(all);
        /**
         * 所有sku的价格集合
         */
        goods.setPrice(priceList);
        /**
         * 所有sku集合的json格式
         */
        goods.setSkus(JsonUtils.toString(skus));
        goods.setSpecs(null);
        goods.setSubTitle(spu.getSubTitle());

        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    @Override
    public PageResult<Goods> search(SearchRequest request) {
        // elasticsearch第一页是0，故需要减1
        int page = request.getPage() - 1;
        int size = request.getSize();
        // 1.创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 2.设置结果过滤（只拿自己想要的字段）
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));

        // 3.分页
        queryBuilder.withPageable(PageRequest.of(page, size));
        // 4.搜索条件
        QueryBuilder basicQuery = QueryBuilders.matchQuery("all", request.getKey());
        queryBuilder.withQuery(basicQuery);

        // 聚合分类和品牌信息
        // 1.聚合分类
        String categoryAggName = "category_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));

        // 2.聚合品牌
        String brandAggName = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));


        // 5.查询
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);

        // 6.解析结果
        // 解析分页结果
        long total = result.getTotalElements();
//        Long totalPages = Long.valueOf(total/request.getDefaultSize());
        Long totalPages = Long.valueOf(result.getTotalPages());
        List<Goods> goodsList = result.getContent();
        // 解析聚合结果
        Aggregations aggregations = result.getAggregations();
        List<Category> categories = parseCategoryAgg(aggregations.get(categoryAggName));
        List<Brand> brands = parseBrandAgg(aggregations.get(brandAggName));

        // 7.完成规格参数聚合
        List<Map<String, Object>> specs = null;
        if (null != categories && categories.size() == 1){
            // 商品分类存在并且数量为1，可以聚合规格参数
            specs = buildSpecificationAgg(categories.get(0).getId(), basicQuery);
        }

        return new SearchResult(total, totalPages, goodsList, categories, brands, specs);
    }

    /**
     * 对规格参数进行聚合并解析结果
     * @param cid
     * @param basicQuery
     * @return
     */
    private List<Map<String, Object>> buildSpecificationAgg(Long cid, QueryBuilder basicQuery) {
        List<Map<String, Object>> specs = new ArrayList<>();
        // 1.查询需要进行聚合的规格参数
        List<SpecParam> params = specificationClient.queryParamByGid(null, cid, true);
        // 2.进行聚合
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 2.1 带上查询条件
        queryBuilder.withQuery(basicQuery);
        // 2.2 聚合
        for (SpecParam param : params) {
            String name = param.getName();
            queryBuilder.addAggregation(
                    AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        }

        // 3.获取结果
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);

        // 4.解析结果
        Aggregations aggregations = result.getAggregations();
        for (SpecParam param : params) {
            // 规格参数名称
            String name = param.getName();
            Terms terms = aggregations.get(name);
            List<String> options = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsString()).collect(Collectors.toList());

            // 准备map
            Map<String, Object> map = new HashMap<>();
            map.put("k", name);
            map.put("options", options);

            specs.add(map);
        }

        return specs;
    }

    private List<Brand> parseBrandAgg(LongTerms terms) {
       try {
           List<Long> ids = terms.getBuckets()
                   .stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
           List<Brand> brands = brandClient.queryBrandByIds(ids);
           return brands;
       }catch (Exception e){
           return null;
       }
    }

    private List<Category> parseCategoryAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Category> categories = categoryClient.queryCategoryListByIds(ids);
            return categories;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void createOrUpdateIndex(Long spuId) {
        // 查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        // 构建商品信息
        Goods goods = buildGoods(spu);
        // 存入索引库
        goodsRepository.save(goods);
    }


    @Override
    public void deleteIndex(Long spuId) {
        goodsRepository.deleteById(spuId);
    }
}
