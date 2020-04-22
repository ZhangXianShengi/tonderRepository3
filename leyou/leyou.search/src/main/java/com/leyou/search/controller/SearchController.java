package com.leyou.search.controller;

import com.leyou.item.entity.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@Controller
public class SearchController {
   /* @Autowired
    private SearchRequest searchRequest;
    @GetMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest searchRequest){
        PageResult<Goods> result = this.searchRequest.search(searchRequest);
        if (result == null || CollectionUtils){

        }
    }*/
}
