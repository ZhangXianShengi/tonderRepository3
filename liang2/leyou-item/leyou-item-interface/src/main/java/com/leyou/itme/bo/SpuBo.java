package com.leyou.itme.bo;

import com.leyou.itme.pojo.Sku;
import com.leyou.itme.pojo.Spu;
import com.leyou.itme.pojo.SpuDetail;

import java.util.List;

public class SpuBo extends Spu {

    //以下属性是spu对象属性的扩张
    private String cname;
    private String bname;

    private SpuDetail spuDetail;

    private List<Sku> skus;

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

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }
}
