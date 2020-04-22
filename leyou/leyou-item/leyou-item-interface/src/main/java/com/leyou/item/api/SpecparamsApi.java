package com.leyou.item.api;

import com.leyou.item.entity.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("spec")
public interface SpecparamsApi {
    @GetMapping("params")
    public List<SpecParam> selectParams(
            @RequestParam(name = "gid" , required = false)Long gid,
            @RequestParam(name = "cid" , required = false)Long cid
    );
}
