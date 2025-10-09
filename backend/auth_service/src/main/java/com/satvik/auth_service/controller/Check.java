package com.satvik.auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Check {

    @GetMapping
    public String check(){
        return "Everything's Alright!!";
    }
}
