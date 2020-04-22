package com.leyou.itme.api;

import com.leyou.itme.pojo.SpecGroup;
import com.leyou.itme.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("spec")
public interface SpecificationApi {

    /**
     * 根据分组id查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public List<SpecParam> queryParam(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam( value = "generic",required = false) Boolean generic,
            @RequestParam(value = "searching",required = false) Boolean searching
    );

    /**
     * 根据分类id查询规格参数组以及对应的参数
     * @param cid
     * @return
     */
    @GetMapping("group/param/{cid}")
    public List<SpecGroup> queryGroupWithParam(@PathVariable("cid") Long cid);
}









































































