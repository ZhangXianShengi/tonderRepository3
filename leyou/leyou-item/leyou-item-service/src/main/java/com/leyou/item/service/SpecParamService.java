package com.leyou.item.service;

import com.leyou.item.entity.SpecParam;

import java.util.List;

public interface SpecParamService {
    List<SpecParam> selectParams(Long gid,Long cid);
}
