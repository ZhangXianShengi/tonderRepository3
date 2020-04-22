package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService  {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPageAndSort(
            Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 开始分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        Page<Brand> pageInfo = (Page<Brand>) brandMapper.selectByExample(example);
        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }
    /**
     * 新增品牌
     * @param brand
     * @return
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        // 新增品牌信息
       Boolean flag= this.brandMapper.insertSelective(brand)==1;
        // 新增品牌和分类中间表
        if (flag){
            cids.forEach(cid->{
                this.brandMapper.insertCategoryBrand(cid, brand.getId());});
        }

    }
    @Transactional
    public void updateBrand(Brand brand, List<Long> cids) {

      /* this.brandMapper.update(brand);


            cids.forEach(cid->{
                this.brandMapper.updateByPrimaryKey(cid, brand.getId());});
        }*/

    }

     //品牌删除


    //@Override
   @Transactional(rollbackFor = Exception.class)
    public void deleteBrand(Long id) {
        //删除品牌信息
        this.brandMapper.deleteByPrimaryKey(id);

        //维护中间表
        this.brandMapper.deleteByBrandIdInCategoryBrand(id);
    }
}