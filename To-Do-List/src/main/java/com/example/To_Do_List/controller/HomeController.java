package com.example.To_Do_List.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Slf4j
@Controller
public class HomeController {
    @GetMapping({"","/","home"})
    public String homePage(){
        log.debug("Отображение главной страницы");
        return "homePage";

    }
}
