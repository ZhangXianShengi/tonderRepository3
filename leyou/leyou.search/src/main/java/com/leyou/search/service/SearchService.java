package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.leyou.item.entity.*;
import com.leyou.search.client.BrandsClient;
import com.leyou.search.client.CategorysClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecparamsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repostory.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {
    @Autowired
    private BrandsClient brandsClient;
    @Autowired
    private CategorysClient categorysClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecparamsClient specparamsClient;
    @Autowired
    private GoodsRepository goodsRepository;

    //字符穿转为json工具  fastJson jackson gson
    //使用工具来进行转换
    private static final ObjectMapper MAPPER = new ObjectMapper();



    //声明一个转变为goods的方法
    public Goods builedGoods(Spu spu) throws JsonProcessingException {
        Goods goods = new Goods();
        //根据spu中的品牌id查询品牌
        Brand brand = this.brandsClient.queryBrandBuId(spu.getBrandId());
        //分类数据查询
        List<String> names = this.categorysClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //根据supid查询spus
        List<Sku> skus = this.goodsClient.querySkusBySpuid(spu.getId());
        //创建一个放入价格的集合
        ArrayList<Long> prices = new ArrayList<>();
        //创建一个map集合用来存放title images id price
        List<Map<String,Object>> skuMapList = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id",sku.getId());
            skuMap.put("title",sku.getTitle());
            skuMap.put("images",StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);
            skuMap.put("price",sku.getPrice());
            skuMapList.add(skuMap);
        });

        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());
        //反序列化
        Map<Long,Object> genericSpecMap=MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long,Object>>(){});
        Map<Long,List<Object>> specialSpecMap=MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long,List<Object>>>(){});
        //查询所有的规格参数查询
        List<SpecParam> specParams = this.specparamsClient.selectParams(null, spu.getCid3());
        HashMap<String, Object> spec = new HashMap<>();
        specParams.forEach(specParam -> {
            if (specParam.getGeneric() && genericSpecMap.get(specParam.getId())!=null){
                Object value= genericSpecMap.get(specParam.getId().toString());
                if (specParam.getNumerical()){
                    value = chooseSegment((String) value, specParam);
                }
                spec.put(specParam.getName(),value);

            }else {
                List<Object> value = specialSpecMap.get(specParam.getId());
                spec.put(specParam.getName(),value);
            }
        });

        BeanUtils.copyProperties(spu,goods);
        goods.setAll(spu.getTitle()+""+brand.getName()+""+ StringUtils.join(names, ""));
        goods.setPrice(prices);
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));//键输入skumapList转成字符串
        goods.setSpecs(spec);
        return goods;
    }

    //分割数字 1-1.5 1.5-2
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
