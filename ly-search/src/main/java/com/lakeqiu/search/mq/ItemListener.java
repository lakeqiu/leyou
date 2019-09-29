package com.lakeqiu.search.mq;

import com.lakeqiu.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听商品更改的消息
 * @author lakeqiu
 */
@Component
public class ItemListener {

    @Autowired
    private SearchService searchService;

    /**
     * 监听商品的增加与修改
     * 根据消息，更新索引
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.insert.queue", durable = "true"),
            exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}
    ))
    public void ListenInsertOrUpdate(Long spuId){
        if (null == spuId){
            return;
        }
        // 根据消息，对索引库进行新增或修改
        searchService.createOrUpdateIndex(spuId);
    }

    /**
     * 监听商品的删除
     * 根据消息，删除索引
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.delete.queue", durable = "true"),
            exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void ListenDelete(Long spuId){
        if (null == spuId){
            return;
        }
        // 根据消息，对索引库对应数据进行删除
        searchService.deleteIndex(spuId);
    }
}
