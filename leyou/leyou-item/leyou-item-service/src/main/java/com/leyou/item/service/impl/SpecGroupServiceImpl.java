package com.leyou.item.service.impl;

import com.leyou.item.entity.SpecGroup;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupServiceImpl implements SpecGroupService {
    @Autowired
    SpecGroupMapper specGroupMapper;

    @Override
    public List<SpecGroup> selectGroups(Long cid) {
        SpecGroup sc = new SpecGroup();
        sc.setCid(cid);
        return specGroupMapper.select(sc);
    }
}
