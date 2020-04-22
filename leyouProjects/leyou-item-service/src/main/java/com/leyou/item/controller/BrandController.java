package com.leyou.item.controller;

import com.leyou.common.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;
    private CategoryServiceImpl categoryService;

    /**
     * 品牌管理
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key){
        PageResult<Brand> result = brandService.queryBrandByPage(key,page,rows,sortBy,desc);
        if(CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 品牌新增
     * @param brand
     * @param cids
     */

    @PostMapping
    public ResponseEntity saveBrand(Brand brand,@RequestParam(value = "cids") List<Long> cids){
        brandService.saveBrand(brand,cids);
        return ResponseEntity.ok().build();
    }


    //品牌查询的回显
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid) {
        List<Category> list = this.categoryService.queryByBrandId(bid);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 删除tb_brand中的数据,单个删除、多个删除二合一
     * @param bid
     * @return
     */
    //删除品牌数据
    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") String bid){
        String separator="-";
        if(bid.contains(separator)){
            String[] ids=bid.split(separator);
            for (String id:ids){
                this.brandService.deleteByBrandIdInCategoryBrand(Long.parseLong(id));
            }
        }
        else {
            this.brandService.deleteByBrandIdInCategoryBrand(Long.parseLong(bid));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
