package com.leyou.controller;

import com.leyou.itme.pojo.Category;
import com.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点id查询子节点
     *
     * @param pid
     * @return
     */
    @GetMapping(value = "list")
    public ResponseEntity<List<Category>> queryByParent(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        //400
        if (pid == null || pid < 0) {
            return ResponseEntity.badRequest().build();
        }
        List<Category> list = categoryService.queryCategoryByPid(pid);
        //404
       /* if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }*/
        return ResponseEntity.ok(list);
    }

    /**
     * 通过品牌id查询商品分类
     *
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid) {
        List<Category> list = this.categoryService.queryByBrandId(bid);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping
    public ResponseEntity<List<String>> queryNameByIds(@RequestParam("ids") List<Long> ids) {
        List<String> names = this.categoryService.queryNameByIds(ids);
        if (CollectionUtils.isEmpty(names)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }

    /**
     * 根据3级分类id，查询1~3级的分类
     * @param id
     * @return
     */
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryAllByCid3(@RequestParam("id") Long id){
        List<Category> list = this.categoryService.queryAllByCid3(id);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(list);
    }
}





















































