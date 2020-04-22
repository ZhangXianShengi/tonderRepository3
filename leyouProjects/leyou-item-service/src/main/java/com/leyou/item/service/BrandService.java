package com.leyou.item.service;

import com.leyou.common.PageResult;
import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BrandService {
    PageResult<Brand> queryBrandByPage(String key, Integer page, Integer rows, String sortBy, boolean desc);

    void saveBrand(Brand brand, List<Long> cids);

    void deleteByBrandIdInCategoryBrand(@Param("bid") Long bid);
}
