package com.leyou.searchs.client;

import org.springframework.cloud.openfeign.FeignClient;
//根据名称调用工程
@FeignClient("item-service")
public interface GoodsClient {

}
