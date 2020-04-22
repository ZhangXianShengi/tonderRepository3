package com.leyou.item.api;

import com.leyou.item.entity.Brand;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("brand")
public interface BrandsApi {
    public Brand queryBrandBuId(@PathVariable("id") Long id);
}
