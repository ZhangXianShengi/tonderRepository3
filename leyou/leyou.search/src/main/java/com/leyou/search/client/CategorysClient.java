package com.leyou.search.client;

import com.leyou.item.api.CategorysApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("item-service")
public interface CategorysClient extends CategorysApi {

    @Override
    default List<String> queryNameByIds(List<Long> ids) {
        return null;
    }
}
