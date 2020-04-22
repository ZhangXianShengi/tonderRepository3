package com.leyou.web.service;

import com.leyou.itme.pojo.*;
import com.leyou.web.client.BrandClient;
import com.leyou.web.client.CategoryClient;
import com.leyou.web.client.GoodsClient;
import com.leyou.web.client.SpecificationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;

    public Map<String,Object> loadData(Long spuId){
        Map<String,Object> model = new HashMap<>();

        // 根据spuId查询spu
        Spu spu = this.goodsClient.querySpuById(spuId);

        // 根据spuId查询spudetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailSpuId(spuId);

        // 根据spuId查询sku集合
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);

        // 查询分类
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNameByIds(cids);
        //初始化一个分类的list<map<String,Object>>
        List<Map<String,Object>> categorys = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cids.get(i));
            map.put("name", names.get(i));
            categorys.add(map);
        }

        // 查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        // 查询规格参数组
        List<SpecGroup> groups = this.specificationClient.queryGroupWithParam(spu.getCid3());

        // 查询特殊的规格参数
        List<SpecParam> params = this.specificationClient.queryParam(null, spu.getCid3(), false, null);
        //初始化一个map存放特殊的规格参数
        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(param -> {
            paramMap.put(param.getId(),param.getName());
        });

        // 封装spu
        model.put("spu", spu);
        // 封装spuDetail
        model.put("spuDetail", spuDetail);
        // 封装sku集合
        model.put("skus", skus);
        // 分类
        model.put("categories", categorys);
        // 品牌
        model.put("brand", brand);
        // 规格参数组
        model.put("groups", groups);
        // 查询特殊规格参数
        model.put("paramMap", paramMap);

        return model;
    }
}














