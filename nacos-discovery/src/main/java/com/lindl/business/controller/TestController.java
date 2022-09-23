package com.lindl.business.controller;

import com.lindl.business.feign.DemoFeign;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping(value = "/discovery")
@RestController
public class TestController {

    @Resource
    DemoFeign demoFeign;

    @GetMapping(value = "/test")
    public String test() {
        return demoFeign.test();
    }

}
