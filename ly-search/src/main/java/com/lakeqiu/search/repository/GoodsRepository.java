package com.lakeqiu.search.repository;

import com.lakeqiu.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author lakeqiu
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
