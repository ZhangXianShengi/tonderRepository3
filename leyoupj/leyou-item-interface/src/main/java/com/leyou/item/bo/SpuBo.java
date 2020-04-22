package com.leyou.item.bo;

import com.leyou.item.pojo.Spu;

public class SpuBo extends Spu {
    private String categoryName;
private String brandName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
