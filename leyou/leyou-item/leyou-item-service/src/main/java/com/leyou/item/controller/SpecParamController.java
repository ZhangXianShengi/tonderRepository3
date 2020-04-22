package com.leyou.item.controller;

import com.leyou.item.entity.SpecGroup;
import com.leyou.item.entity.SpecParam;
import com.leyou.item.service.SpecGroupService;
import com.leyou.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("spec")
public class SpecParamController {

    @Autowired
    SpecGroupService specGroupService;

    @Autowired
    SpecParamService specParamService;

    /**
     * 根据分类id查询参数组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> selectGroups(@PathVariable("cid")Long cid){
        List<SpecGroup> specGroups = specGroupService.selectGroups(cid);
        if(CollectionUtils.isEmpty(specGroups)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(specGroups);
    }

    /**
     * 根据分组id查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> selectParams(
            @RequestParam(name = "gid" , required = false)Long gid,
            @RequestParam(name = "cid" , required = false)Long cid

    ){
        List<SpecParam> specGroups = specParamService.selectParams(gid,cid);
        if(CollectionUtils.isEmpty(specGroups)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(specGroups);
    }


}
