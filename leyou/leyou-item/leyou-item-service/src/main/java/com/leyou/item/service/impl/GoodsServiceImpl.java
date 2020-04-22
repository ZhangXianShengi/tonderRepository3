package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.entity.*;
import com.leyou.item.mapper.*;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    SpuMapper spuMapper;

    @Autowired
    SpuDetailMapper spuDetailMapper;

    @Autowired
    BrandMapper brandMapper;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SkuMapper skuMapper;

    @Autowired
    StockMapper stockMa;
    /**
     * 查询商品分类及品牌，分类信息
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<SpuBo> querySpuByPage(String key,Boolean saleable,Integer page, Integer rows) {
        //添加查询条件
        Example example = new Example(Spu.class);
        //创建查询条件
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(key)){
            //查询哪个字段  ,  前台传过来的参数   比如前台传一个name  那么就在数据库查询name
            criteria.andLike("title","%"+key+"%");
        }
        //添加上下架的过滤条件
        if(saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }
        //添加分页
        PageHelper.startPage(page,rows);

        //执行查询获取spu集合
        List<Spu> spus = spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);
        //查询出结果集之后根据里面的外键查询出品牌和分类的名称
        //spu就相当于fore里面的每一个对象
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            //将spu对象转换为spuBo
            BeanUtils.copyProperties(spu, spuBo);
            //查询品牌信息
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            //查询分类信息
            List<String> names = categoryService.selectCategoryById(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names,"/"));
            return spuBo;
        }).collect(Collectors.toList());
        //返回
        return new PageResult<>(pageInfo.getTotal(),spuBos);
    }

    /**
     * 新增商品
     * @param spuBo
     */
    @Override
    @Transactional
    public void addGoods(SpuBo spuBo) {
        //新增spu表
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);

        //新增spuDetail表
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        spuDetailMapper.insertSelective(spuDetail);

        //新增sku
        //先将sku分割
        saveStock(spuBo);
    }


    /**
     * 通过spuid查询sku集合
     * @param spuId
     * @return
     */
    @Override
    public List<Sku> querySkusBySpuid(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = skuMapper.select(sku);
        skus.forEach(sku1 -> {
            //查询库存
            Stock stock = stockMa.selectByPrimaryKey(sku1.getId());
            sku1.setStock(stock.getStock());
        });
        return skus;
    }

    /**
     * 根据spuid
     * 查询spudetail
     * @param spuId
     * @return
     */
    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }
    //添加库存通用方法
    void saveStock(SpuBo spuBo){
        spuBo.getSkus().forEach(sku -> {
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insert(sku);
            //新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMa.insertSelective(stock);
        });
    }
    /**
     * 保存商品
     * @param spuBo
     */
    @Override
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        //先删除stock
        Sku record = new Sku();
        //只查询此spu拥有的spu
        record.setSpuId(spuBo.getId());
        //查询spu包含的sku
        List<Sku> skus = skuMapper.select(record);
        skus.forEach(sku -> {
            //sku主键就是库存，删除库存
            stockMa.deleteByPrimaryKey(sku.getId());
        });
        //删除sku
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());
        skuMapper.delete(sku);

        //sku
        saveStock(spuBo);
        //新增stock

        //更新spu和spudetail
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        spuMapper.updateByPrimaryKeySelective(spuBo);
        spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
    }
}