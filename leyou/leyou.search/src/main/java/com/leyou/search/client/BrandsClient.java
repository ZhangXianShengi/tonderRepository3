package com.leyou.search.client;

import com.leyou.item.api.BrandsApi;
import com.leyou.item.entity.Brand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("item-service")
public interface BrandsClient extends BrandsApi {

    @Override
    default Brand queryBrandBuId(Long id) {
        return null;
    }
}
