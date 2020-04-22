package com.leyou.service.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.commen.PageResult;
import com.leyou.intarface.bo.SpuBo;
import com.leyou.intarface.pojo.Sku;
import com.leyou.intarface.pojo.Spu;
import com.leyou.intarface.pojo.SpuDetail;
import com.leyou.service.mapper.BrandMapper;
import com.leyou.service.mapper.SkuMapper;
import com.leyou.service.mapper.SpuDetailMapper;
import com.leyou.service.mapper.SpuMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<SpuBo> querySpuBoByPage(String key, Boolean saleable, Integer page, Integer rows) {

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        // 搜索条件
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        // 分页条件
        PageHelper.startPage(page, rows);

        // 执行查询
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        List<SpuBo> spuBos = new ArrayList<>();
        spus.forEach(spu->{
            SpuBo spuBo = new SpuBo();
            // copy共同属性的值到新的对象
            BeanUtils.copyProperties(spu, spuBo);
            // 查询分类名称
            List<String> names = this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names, "/"));

            // 查询品牌的名称
            spuBo.setBname(this.brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());

            spuBos.add(spuBo);
        });

        return new PageResult<>(pageInfo.getTotal(), spuBos);

    }

    public SpuDetail querySpuDetailById(Long supId) {
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(supId);
        return this.spuDetailMapper.selectByPrimaryKey(spuDetail);
    }
    //根据spu id 查询sku
    public List<Sku> querySkuBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        return this.skuMapper.select(sku);
    }
}