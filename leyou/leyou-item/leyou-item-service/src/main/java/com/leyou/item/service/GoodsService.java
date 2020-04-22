package com.leyou.item.service;

import com.leyou.item.bo.SpuBo;
import com.leyou.item.entity.PageResult;
import com.leyou.item.entity.Sku;
import com.leyou.item.entity.SpuDetail;

import java.util.List;

public interface GoodsService {
    PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows);

    void addGoods(SpuBo spuBo);


    List<Sku> querySkusBySpuid(Long spuId);

    SpuDetail querySpuDetailBySpuId(Long spuId);

    void saveGoods(SpuBo spuBo);
}
