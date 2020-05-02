package com.springsecurityjwt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public String hello(){
        return "Hello world of Corona!! How are you?";
    }
}
