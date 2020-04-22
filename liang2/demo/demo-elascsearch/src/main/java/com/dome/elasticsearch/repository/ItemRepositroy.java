package com.dome.elasticsearch.repository;

import com.dome.elasticsearch.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepositroy extends ElasticsearchRepository<Item,Long> {

    //自定义方法
    List<Item> findByTitle(String title);
    List<Item> findByPriceBetween(double price1,double price2);
}
