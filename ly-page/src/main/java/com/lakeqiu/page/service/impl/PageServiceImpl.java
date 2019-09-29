package com.lakeqiu.page.service.impl;

import com.lakeqiu.item.pojo.*;
import com.lakeqiu.page.client.BrandClient;
import com.lakeqiu.page.client.CategoryClient;
import com.lakeqiu.page.client.GoodsClient;
import com.lakeqiu.page.client.SpecificationClient;
import com.lakeqiu.page.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lakeqiu
 */
@Slf4j
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public Map<String, Object> loadModel(Long spuId) {
        Map<String, Object> map = new HashMap<>();
        // 查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        // 查询skus
        List<Sku> skus = spu.getSkus();
        // 查询detail（商品描述）
        SpuDetail spuDetail = spu.getSpuDetail();
        // 查询brand
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        // 查询商品分类
        List<Category> categories = categoryClient.queryCategoryListByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        // 查询规格参数
        List<SpecGroup> specGroups = specificationClient.queryGroupByCid(spu.getCid3());

        map.put("spu", spu);
        map.put("skus", skus);
        map.put("detail", spuDetail);
        map.put("brand", brand);
        map.put("categories", categories);
        map.put("specs", specGroups);

        return map;
    }


    @Override
    public void createHtml(Long spuId) {
        // 获取上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));

        // 获取输出流
        File dest = new File("E:\\newcode\\you\\upload", spuId + ".html");

        // 如果这个静态模板存在，删除，重新写入
        if (dest.exists()){
            dest.delete();
        }

        try(PrintWriter writer = new PrintWriter(dest, "utf-8")){
            // 生成Html
            templateEngine.process("item", context, writer);
        }catch (Exception e){
            log.error("【静态页服务】生成静态页面异常", e);
        }

    }

    @Override
    public void deleteHtml(Long spuId) {
        // 获取输出流
        File dest = new File("E:\\newcode\\you\\upload", spuId + ".html");

        // 如果这个静态模板存在，删除，重新写入
        if (dest.exists()){
            dest.delete();
        }
    }
}
