package com.leyou.item.service;

import com.leyou.item.entity.Brand;
import com.leyou.item.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> selectCategory(Long id);
    List<Category> selectCategoryById(Long id);
    Brand queryBrandById(Long id);

    List<String> selectCategoryById(List<Long> ids);

}

