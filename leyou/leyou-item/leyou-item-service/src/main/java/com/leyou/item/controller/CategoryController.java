package com.leyou.item.controller;

import com.leyou.item.entity.Brand;
import com.leyou.item.entity.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 查询分类，返回集合
     * 最为统一的返回值类型
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> selectCategory(
            @RequestParam(value = "pid",defaultValue = "0",required = true)Long pid
            ){
            if(pid==null || pid<0){
                //返回400，参数不合法
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Category> categories = categoryService.selectCategory(pid);
            if(CollectionUtils.isEmpty(categories)){
                //查询为空返回404
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(categories);
    }

    /**
     * 修改:查询品牌
     * @param id 品牌id
     * @return
     */
    @GetMapping("bid/{id}")
    public ResponseEntity<List<Category>> getCategoryById(@PathVariable("id")Long id){
        List<Category> categories = categoryService.selectCategoryById(id);
        Brand brand = categoryService.queryBrandById(id);
        for (Category category:categories
             ) {
            category.setBrand(brand);
        }
        if(CollectionUtils.isEmpty(categories)){
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(categories);
    }
    @GetMapping
    public ResponseEntity<List<String>> queryNameByIds(@RequestParam("ids") List<Long> ids){
        List<String> names = this.categoryService.selectCategoryById(ids);
        if (CollectionUtils.isEmpty(names)){
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(names);
    }
}
