package com.leyou.web.listener;

import com.leyou.web.service.GoodsHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {

    @Autowired
    private GoodsHtmlService goodsHtmlServicel;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.GOODS.SAVE.QUEUE",durable = "true"),
            exchange = @Exchange(name = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void saveListener(Long spuId){
        if (spuId == null){
            return;
        }
        //重新生成静态页面
        goodsHtmlServicel.createHtml(spuId);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.GOODS.DELETE.QUEUE",durable = "true"),
            exchange = @Exchange(name = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void deleteListener(Long spuId){
        if (spuId == null){
            return;
        }
        //重新生成静态页面
        goodsHtmlServicel.deleteHtml(spuId);
    }
}



























