package com.leyou.item.service;

import com.leyou.item.entity.SpecGroup;

import java.util.List;

public interface SpecGroupService {
    List<SpecGroup> selectGroups(Long cid);
}
