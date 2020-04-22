package com.leyou.item.api;

import com.leyou.item.bo.SpuBo;
import com.leyou.item.entity.PageResult;
import com.leyou.item.entity.Sku;
import com.leyou.item.entity.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//对外提供的接口
public interface GoodsApi{

    @GetMapping("spu/page")
    public PageResult<SpuBo> selectGoods(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "saleable",defaultValue = "true")Boolean saleable,
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows

    );

    @GetMapping("spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId")Long spuId);

    /**
     * 通过spuid查询sku集合
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public List<Sku> querySkusBySpuid(@RequestParam(value = "id")Long spuId);


}


