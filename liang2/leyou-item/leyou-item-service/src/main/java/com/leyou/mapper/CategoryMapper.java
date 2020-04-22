package com.leyou.mapper;

import com.leyou.itme.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
//Category 是返回值的类型
//Long 主键的类型
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {
    /**
     * 通过品牌id查询商品分类
     * @param bid
     * @return
     */
    @Select("SELECT * FROM tb_category WHERE id IN (SELECT category_id FROM tb_category_brand WHERE brand_id = #{bid})")
    List<Category> queryByBrandId(Long bid);
}
