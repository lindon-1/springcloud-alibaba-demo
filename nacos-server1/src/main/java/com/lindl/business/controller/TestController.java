package com.lindl.business.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@RefreshScope
public class TestController {

    @Value("${name}")
    String name;

    @Value("${address}")
    String address;

    @Value("${age}")
    Integer age;

    @Value("${server.port:80}")
    Integer port;

    @GetMapping(value ="/test")
    public String test() {
        System.out.println(name);
        return "端口号：" + + port + " name:" + name + ", age: " + age + ", address:" +address ;
    }



}
