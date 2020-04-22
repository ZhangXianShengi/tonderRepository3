package com.leyou.web.controller;

import com.leyou.web.service.GoodsHtmlService;
import com.leyou.web.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class GoodsContronller {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * 跳转到商品详情页
     * @param id
     * @param model
     * @return
     */
    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id") Long id, Model model){
        Map<String, Object> map = this.goodsService.loadData(id);
        model.addAllAttributes(map);
        //调用生成的静态页面
        this.goodsHtmlService.createHtml(id);
        return "item";
    }

}




























