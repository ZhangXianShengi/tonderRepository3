package com.demo.zyf.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello1")
public class HellowSpringBoot2 {
    @GetMapping("boot1")
    public String testSpringBoot() {
        return "恭喜，您的第二个程序搭建成功！";
    }
}
