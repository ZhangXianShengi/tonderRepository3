package com.leyou.search.clients;

import com.leyou.intarface.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}