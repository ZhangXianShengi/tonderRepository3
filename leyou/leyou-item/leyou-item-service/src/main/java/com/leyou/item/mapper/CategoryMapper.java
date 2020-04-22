package com.leyou.item.mapper;

import com.leyou.item.entity.Brand;
import com.leyou.item.entity.Category;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

//category 返回值类型
//long 主键类型
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {
        @Select("select * from tb_category where id in(select category_id from tb_category_brand where brand_id=#{id})")
        @Results({
                @Result(id = true , property = "id" , column = "id"),
                @Result(property = "name",column = "name"),
                @Result(property = "parentId",column = "parentId"),
                @Result(property = "isParent",column = "isParent"),
                @Result(property = "sort",column = "sort")
        })
        List<Category> queryCategoryById(Long id);

        @Select("select * from tb_brand where id = #{id}")
        Brand queryBrandById(Long id);
}
