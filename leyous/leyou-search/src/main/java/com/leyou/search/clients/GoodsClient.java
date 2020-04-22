package com.leyou.search.clients;

import com.leyou.intarface.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}