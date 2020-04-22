package com.leyou.item.bo;

import com.leyou.item.entity.Sku;
import com.leyou.item.entity.Spu;
import com.leyou.item.entity.SpuDetail;

import javax.persistence.Transient;
import java.util.List;

public class SpuBo extends Spu {
    private String cname;//商品分类名称
    private String bname;//品牌名称
    @Transient
    SpuDetail spuDetail;// 商品详情
    @Transient
    List<Sku> skus;// sku列表

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}
