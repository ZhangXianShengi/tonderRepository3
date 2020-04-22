package com.leyou.itme.api;

import com.leyou.itme.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("brand")
public interface BrandApi {

    /**
     * 根据品牌id查询品牌对象
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id")Long id);
}


























