package com.leyou.search.pojo;

import com.leyou.item.entity.PageResult;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.querydsl.QPageRequest;

public class SearchRequest {
 /*   private String key;// 搜索条件

    private Integer page;// 当前页

    private static final Integer DEFAULT_SIZE = 20;// 每页大小，不从页面接收，而是固定大小
    private static final Integer DEFAULT_PAGE = 1;// 默认页

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(page == null){
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }

    public PageResult<Goods> search(SearchRequest searchRequest) {
        //判断用户查询的数据  判断用户查询的数据是否为空
        if (StringUtils.isBlank(searchRequest.getKey())){
                return null;
        }
        //自定义查询构建起
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",searchRequest.getKey()).operator(Operator.AND));
        //添加分页
        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage()-1,searchRequest.getSize()));
        //添加结果集过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));
    }*/
}