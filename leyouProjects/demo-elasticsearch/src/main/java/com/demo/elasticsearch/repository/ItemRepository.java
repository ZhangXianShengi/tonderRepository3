package com.demo.elasticsearch.repository;

import com.demo.elasticsearch.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepository extends ElasticsearchRepository<Item,Long> {
    //自定义方法   不需要实现Repository自动生成代理
    List<Item> findByTitle(String title);
    List<Item> findByPriceBetween(Double d1,Double d2);
}
