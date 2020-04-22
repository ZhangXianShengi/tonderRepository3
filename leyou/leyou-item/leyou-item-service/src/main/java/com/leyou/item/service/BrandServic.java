package com.leyou.item.service;

import com.leyou.item.entity.Brand;
import com.leyou.item.entity.PageResult;

import java.util.List;

public interface BrandServic {
    PageResult<Brand> selectBrand(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    void saveBrand(Brand brand, List<Long> cids);

    void deleteBrandCategory(Long id);

    List<Brand> queryCategortByBrand(Long cid);

    Brand queryBrandBuId(Long id);
}
