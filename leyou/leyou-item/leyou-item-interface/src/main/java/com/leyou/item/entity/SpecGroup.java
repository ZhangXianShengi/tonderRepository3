package com.leyou.item.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Table(name = "tb_spec_group")
public class SpecGroup {

    @Id
     private Long id;
    private Long cid;
    private String name;//分组名称

    @Transient
    private List<SpecParam> params;

    public List<SpecParam> getParams() {
        return params;
    }

    public void setParams(List<SpecParam> params) {
        this.params = params;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}