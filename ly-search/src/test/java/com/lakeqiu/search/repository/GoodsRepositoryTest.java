package com.lakeqiu.search.repository;

import com.lakeqiu.common.vo.PageResult;
import com.lakeqiu.item.pojo.Spu;
import com.lakeqiu.search.client.GoodsClient;
import com.lakeqiu.search.pojo.Goods;
import com.lakeqiu.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author lakeqiu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Test
    public void testCreateIndex(){
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    @Test
    public void loadData(){
        int page = 1;
        int rows = 100;
        // 查询出来的当前页条数
        int size = 0;
       do {
           // 查询spu
           PageResult<Spu> pageResult = goodsClient.querySpuByPage(page, rows, true, null);
           List<Spu> spuList = pageResult.getItems();

           // 防止最后一次查询刚好查到100条，后面没数据了，跳出
           if (CollectionUtils.isEmpty(spuList)){
               break;
           }

           // 构建goods
           List<Goods> goodsList = spuList.stream()
                   .map(searchService::buildGoods).collect(Collectors.toList());

           // 存入索引库
           goodsRepository.saveAll(goodsList);

           // 查询出来的当前页条数，不等于100，说明没有插满，是最后一页
           page++;
           size = spuList.size();
       }while (size == 100);

    }
}