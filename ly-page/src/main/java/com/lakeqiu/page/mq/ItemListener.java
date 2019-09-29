package com.lakeqiu.page.mq;

import com.lakeqiu.page.service.PageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听商品状态的更改
 * @author lakeqiu
 */
@Component
public class ItemListener {

    @Autowired
    private PageService pageService;

    /**
     * 监听商品的增加与修改
     * 根据消息，更新索引
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "page.item.insert.queue", durable = "true"),
            exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}
    ))
    public void ListenInsertOrUpdate(Long spuId){
        if (null == spuId){
            return;
        }
        // 根据消息，生成新的静态模板
        pageService.createHtml(spuId);
    }


    /**
     * 监听商品的删除
     * 根据消息，删除索引
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "page.item.delete.queue", durable = "true"),
            exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void ListenDelete(Long spuId){
        if (null == spuId){
            return;
        }
        // 根据消息，对相应静态模板进行删除
        pageService.deleteHtml(spuId);
    }
}
