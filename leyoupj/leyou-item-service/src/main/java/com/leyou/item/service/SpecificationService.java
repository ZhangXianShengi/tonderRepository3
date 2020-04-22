package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup specGroup=new SpecGroup();
        specGroup.setCid(cid);
        return this.specGroupMapper.select(specGroup);
    }

    public List<SpecParam> queryParamByCid(Long gid) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        return  this.specParamMapper.select(specParam);
    }
}
