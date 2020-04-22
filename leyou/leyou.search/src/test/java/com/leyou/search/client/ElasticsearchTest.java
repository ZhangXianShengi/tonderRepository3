package com.leyou.search.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.entity.PageResult;
import com.leyou.search.LeyouSearchService;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repostory.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouSearchService.class)
public class ElasticsearchTest {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate template;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SearchService searchService;
    @Test
    public void createIndex() {
        // 创建索引库，以及映射
        this.template.createIndex(Goods.class);
        this.template.putMapping(Goods.class);
        Integer page = 1;
        Integer rows = 100;
        do {
            PageResult<SpuBo> result = this.goodsClient.selectGoods(page, true,null, rows);
            List<SpuBo> items = result.getItems();
            List<Goods> goodsList = items.stream().map(spuBo -> {
                try {
                    return this.searchService.builedGoods(spuBo);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            this.goodsRepository.saveAll(goodsList);
            rows = items.size();
            page++;
        } while (rows==100);
    }
}