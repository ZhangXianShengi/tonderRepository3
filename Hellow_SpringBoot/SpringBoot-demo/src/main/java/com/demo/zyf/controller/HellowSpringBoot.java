package com.demo.zyf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@RequestMapping("hello")
public class HellowSpringBoot {
    @Autowired
    private DataSource dataSource;
    @GetMapping("boot")
    public String testSpringBoot() {
        return "恭喜，您的第一个程序搭建成功！";
    }
}
