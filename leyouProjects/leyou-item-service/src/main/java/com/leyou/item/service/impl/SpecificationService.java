package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecificationService {
    @Autowired
    private SpecificationMapper specificationMapper;

    public Object queryById(Long id) {
        return this.specificationMapper.selectByPrimaryKey(id);
    }
}
