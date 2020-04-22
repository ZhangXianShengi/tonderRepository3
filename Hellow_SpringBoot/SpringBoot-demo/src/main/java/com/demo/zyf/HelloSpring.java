package com.demo.zyf;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hellow")
@SpringBootApplication
public class HelloSpring {
    @GetMapping("bootw")
    public String testSpringBoot() {
        return "你的第一次没了";
    }
    public static void main(String[] args) {
        SpringApplication.run(HelloSpring.class,args);
    }
}
