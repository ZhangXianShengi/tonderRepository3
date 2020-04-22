package com.leyou.item.mapper;

import com.leyou.item.entity.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

        /**
         * 新增商品分类和品牌中间表数据
         * @param cid 商品分类id
         * @param bid 品牌id
         * @return
         */
        @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
        int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

        @Delete("delete from  tb_category_brand WHERE brand_id = #{bid}")
        void deleteBrandCategory(Long id);
    @Select("SELECT * FROM tb_category_brand  a INNER JOIN tb_brand b ON a.`brand_id`=b.`id` WHERE a.`category_id`=#{cid}")
    List<Brand> queryCategortByBrand(Long cid);
}
