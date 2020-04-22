package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 品牌管理
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */

    @Override
    public PageResult<Brand> queryBrandByPage(String key, Integer page, Integer rows, String sortBy, boolean desc) {
        //分页
        PageHelper.startPage(page,rows);
        //Example初始化
        Example example = new Example(Brand.class);
        //获取Criteria对象进行sql拼接
        Example.Criteria criteria = example.createCriteria();
        //判断搜索条件是否为空，不为空进行sql拼接查询
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);
        }
        //判断首字母是否为空，添加排序条件
        if(StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy + (desc ? " DESC" : " ASC"));
        }
        //查询结果
        List<Brand> brands = brandMapper.selectByExample(example);
        //把查询结果包装成PageInfo
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        //包装成分页结果集进行返回
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 品牌新增
     * @param brand
     * @param cids
     */

    @Override
    public void saveBrand(Brand brand, List<Long> cids) {
        //品牌增加
        Boolean aBoolean=brandMapper.insertSelective(brand)==1;
        //中间表增加
        if(aBoolean){
            cids.forEach(cid->{
                brandMapper.insertCategoryBrand(cid,brand.getId());
            });
        }

    }

    @Override
    public void deleteByBrandIdInCategoryBrand(Long bid) {
        //删除品牌信息
        this.brandMapper.deleteByPrimaryKey(bid);

        //维护中间表
        this.brandMapper.deleteByBrandIdInCategoryBrand(bid);
    }

}
