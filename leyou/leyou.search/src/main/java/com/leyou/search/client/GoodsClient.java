package com.leyou.search.client;

import com.leyou.item.api.GoodsApi;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.entity.PageResult;
import com.leyou.item.entity.Sku;
import com.leyou.item.entity.SpuDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
    /**
     * 根据分页条件查询spu
     * @param page
     * @param saleable
     * @param key
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    public PageResult<SpuBo> selectGoods(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "saleable", defaultValue = "true") Boolean saleable,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );
    //根据id查询
    @Override
    default SpuDetail querySpuDetailBySpuId(Long spuId) {
        return null;
    }
    //根据spuid查询sku集合
    @Override
    default List<Sku> querySkusBySpuid(Long spuId) {
        return null;
    }
}
