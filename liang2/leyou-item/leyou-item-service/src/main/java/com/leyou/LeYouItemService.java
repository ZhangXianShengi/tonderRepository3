package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.leyou.mapper") //用来扫描Mapper
public class LeYouItemService {
    public static void main(String[] args) {
        SpringApplication.run(LeYouItemService.class, args);
    }
}
