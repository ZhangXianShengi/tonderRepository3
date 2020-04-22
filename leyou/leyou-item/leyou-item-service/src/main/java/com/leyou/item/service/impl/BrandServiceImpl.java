package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.item.entity.Brand;
import com.leyou.item.entity.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.service.BrandServic;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandServic {
    @Autowired
    BrandMapper brandMapper;
    @Override
    public PageResult<Brand> selectBrand(Integer page, Integer rows, String sortBy, Boolean desc, String key) {

        // 过滤
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }

        PageHelper.startPage(page,rows);

        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            example.setOrderByClause(sortBy + (desc ? " desc" : " asc"));
        }
        // 查询
        List<Brand> brands = brandMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo<>(brands);
        // 返回结果
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        // 新增品牌信息
        this.brandMapper.insertSelective(brand);
        // 新增品牌和分类中间表
        for (Long cid : cids) {
            this.brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    @Override
    public void deleteBrandCategory(Long id) {
        //维护中间表
        brandMapper.deleteBrandCategory(id);
        //删除品牌信息
        brandMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据分类id查询品牌
     * @param cid
     * @return
     */
    @Override
    public List<Brand> queryCategortByBrand(Long cid) {
        return brandMapper.queryCategortByBrand(cid);
    }

    @Override
    public Brand queryBrandBuId(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }


}
