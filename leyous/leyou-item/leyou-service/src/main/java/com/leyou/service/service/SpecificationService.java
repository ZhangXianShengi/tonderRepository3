package com.leyou.service.service;

import com.leyou.intarface.pojo.SpecGroup;
import com.leyou.intarface.pojo.SpecParam;
import com.leyou.service.mapper.SpecGroupMapper;
import com.leyou.service.mapper.SpecParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper groupMapper;
    @Autowired
    private SpecParamMapper paramMapper;

    /**
     * 根据分类id查询分组
     *
     * @param cid
     * @return
     */
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.groupMapper.select(specGroup);
    }


    /**
     * 根据条件查询规格参数
     *
     * @param gid
     * @return
     */
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        return this.paramMapper.select(specParam);
    }
}