package com.lindl.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("nacos-server1")
public interface DemoFeign {

    @GetMapping(value = "/nacos/demo/test")
    public String test();
}
