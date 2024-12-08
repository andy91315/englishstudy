package com.englishstudy.englishstudy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnglishController {

    @GetMapping("/index")
    public String index() {
        return "index"; // 返回视图名称
    }
}
