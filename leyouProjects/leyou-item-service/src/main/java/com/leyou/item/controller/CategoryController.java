package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点id查询子节点
     * @param pid
     * @return
     */
    @GetMapping(value = "list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam(value = "pid",defaultValue = "0")Long pid){
        //400
        if(pid == null || pid < 0){
           return ResponseEntity.badRequest().build();
        }
        List<Category> list = categoryService.queryByParentId(pid);
        //404
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }
}
