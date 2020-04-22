package com.leyou.controller;

import com.leyou.itme.pojo.SpecGroup;
import com.leyou.itme.pojo.SpecParam;
import com.leyou.service.SpecificationService;
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
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类id查询参数组
     * @param cid 分类id
     * @return gorups
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryCategoryByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> groups = this.specificationService.queryCategoryByCid(cid);
        if (CollectionUtils.isEmpty(groups)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();//404
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 根据分组id查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParam(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam( value = "generic",required = false) Boolean generic,
            @RequestParam(value = "searching",required = false) Boolean searching
    ){
        List<SpecParam> params =this.specificationService.queryParam(gid,cid,generic,searching);
        if (CollectionUtils.isEmpty(params)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();//404
        }
        return ResponseEntity.ok(params     );
    }

    /**
     * 根据分类id查询规格参数组以及对应的参数
     * @param cid
     * @return
     */
    @GetMapping("group/param/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupWithParam(@PathVariable("cid") Long cid){
        List<SpecGroup> groups = this.specificationService.queryGroupWithParam(cid);
        if (CollectionUtils.isEmpty(groups)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();//404
        }
        return ResponseEntity.ok(groups);
    }
}









































































