package com.leyou.itme.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping(value = "category")
public interface CategoryApi {

    @GetMapping
    public List<String> queryNameByIds(@RequestParam("ids") List<Long> ids);
}





















































