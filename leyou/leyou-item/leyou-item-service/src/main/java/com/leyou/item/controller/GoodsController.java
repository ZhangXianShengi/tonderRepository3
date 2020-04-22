package com.leyou.item.controller;

import com.leyou.item.bo.SpuBo;
import com.leyou.item.entity.PageResult;
import com.leyou.item.entity.Sku;
import com.leyou.item.entity.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    GoodsService goodsService;


    /**
     * 根据分页条件查询spu
     * @param page
     * @param saleable
     * @param key
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> selectGoods(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "saleable",defaultValue = "true")Boolean saleable,
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows

    ){
        PageResult<SpuBo> spuBos = this.goodsService.querySpuByPage(key,saleable,page,rows);
        if(CollectionUtils.isEmpty(spuBos.getItems())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(spuBos);
    }

    /**
     * 新增商品
     */
    @PostMapping("goods")
    public ResponseEntity<Void> addGoods(@RequestBody SpuBo spuBo){
        goodsService.addGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId")Long spuId){
        SpuDetail spuDetail = this.goodsService.querySpuDetailBySpuId(spuId);
        if (spuDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }
    /**
     * 通过spuid查询sku集合
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkusBySpuid( @RequestParam(value = "id")Long spuId){
            List<Sku> skus = goodsService. querySkusBySpuid(spuId);
            if(CollectionUtils.isEmpty(skus)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(skus);
    }

    @PutMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
