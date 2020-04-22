package com.leyou.item.service.impl;

import com.leyou.item.entity.Brand;
import com.leyou.item.entity.Category;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 根据父节点查询子节点
     * @param id
     * @return
     */
    @Override
    public List<Category> selectCategory(Long id) {
        Category category = new Category();
        category.setParentId(id);
        return categoryMapper.select(category);
    }

    @Override
    public List<Category> selectCategoryById(Long id) {
        return categoryMapper.queryCategoryById(id);
    }

    @Override
    public Brand queryBrandById(Long id) {
        return categoryMapper.queryBrandById(id);
    }

    /**
     * 根据spu查询品牌  ， 多个子节点查询方式
     * @param ids
     * @return
     */
    @Override
    public List<String> selectCategoryById(List<Long> ids) {
        List<Category> categories = categoryMapper.selectByIdList(ids);
        List<String> categorie = categories.stream().map(category -> {
            return category.getName();
        }).collect(Collectors.toList());
        return categorie;
    }

}
