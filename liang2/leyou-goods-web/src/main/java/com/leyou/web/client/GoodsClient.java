package com.leyou.web.client;

import com.leyou.itme.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {

}


























