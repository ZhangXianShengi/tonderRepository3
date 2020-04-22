package com.leyou.web.client;

import com.leyou.itme.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
