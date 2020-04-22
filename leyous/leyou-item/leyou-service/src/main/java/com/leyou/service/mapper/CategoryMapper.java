package com.leyou.service.mapper;

import com.leyou.intarface.pojo.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;


public interface CategoryMapper extends Mapper<Category> , SelectByIdListMapper<Category, Long> {
}
