package com.leyou.item.service.impl;

import com.leyou.item.entity.SpecParam;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamServiceImpl implements SpecParamService {
    @Autowired
    SpecParamMapper specParamMapper;

    @Override
    public List<SpecParam> selectParams(Long gid,Long cid) {

        SpecParam sc = new SpecParam();
        sc.setGroupId(gid);
        sc.setCid(cid);
        //gid放谁进去只查询和gid相同的数据
        return specParamMapper.select(sc);
    }
}
