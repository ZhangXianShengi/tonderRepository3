package com.leyou.service.controller;

import com.leyou.commen.PageResult;
import com.leyou.intarface.bo.SpuBo;
import com.leyou.intarface.pojo.Sku;
import com.leyou.intarface.pojo.SpuDetail;
import com.leyou.service.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuBoByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "saleable", required = false)Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows
    ){
        PageResult<SpuBo> pageResult = this.goodsService.querySpuBoByPage(key, saleable, page, rows);
        if(CollectionUtils.isEmpty(pageResult.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pageResult);
    }
    /**
     * 根据spu商品id查询详情
     * @param supId
     * @return
     */
    @GetMapping("/spu/detail/{supId}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("supId") Long supId){
        SpuDetail spuDetail=this.goodsService.querySpuDetailById(supId);
        if (spuDetail==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id){
        List<Sku> skuId = this.goodsService.querySkuBySpuId(id);
       if (skuId==null){
           return ResponseEntity.notFound().build();
       }
       return  ResponseEntity.ok(skuId);
    }

}