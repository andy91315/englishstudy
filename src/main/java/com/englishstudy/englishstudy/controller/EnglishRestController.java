package com.englishstudy.englishstudy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnglishRestController {

    @RequestMapping("/test")
    public String testRest() {
        return "hello world";
    }
}
