package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMaapper;

    /**
     * 根据父节点id查询子节点
     * @param pid
     * @return
     */
    @Override
    public List<Category> queryByParentId(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return this.categoryMaapper.select(category);
    }

    public List<Category> queryByBrandId(Long bid) {
        return this.categoryMaapper.queryByBrandId(bid);
    }

}
