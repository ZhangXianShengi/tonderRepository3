package com.leyou.item.controller;

import com.leyou.item.entity.Brand;
import com.leyou.item.entity.PageResult;
import com.leyou.item.service.BrandServic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("brand")
public class BrandController {

    @Autowired
    BrandServic brandServic;

    /**
     * 品牌的查询
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> selectBrand(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key
    ){
        PageResult<Brand> brandPageResult = brandServic.selectBrand(page, rows, sortBy, desc, key);
        if(brandPageResult==null || brandPageResult.getItems().size()==0){
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(brandPageResult);
    }

    /**
     * 添加品牌
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        brandServic.saveBrand(brand,cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
}

    /**
     * 根据id删除品牌
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    public ResponseEntity<Void> deleteBrand(@RequestParam(value="id",defaultValue = "1",required = true)Long id){
        brandServic.deleteBrandCategory(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> selectCategortByBrand(@PathVariable("cid")Long cid){
        List<Brand> brands = brandServic.queryCategortByBrand(cid);
        if(CollectionUtils.isEmpty(brands)){
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(brands);
    }
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandBuId(@PathVariable("id") Long id){
       Brand brand= this.brandServic.queryBrandBuId(id);
       if (brand==null){
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(brand);
    }
}
