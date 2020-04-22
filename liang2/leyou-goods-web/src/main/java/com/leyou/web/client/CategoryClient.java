package com.leyou.web.client;

import com.leyou.itme.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
