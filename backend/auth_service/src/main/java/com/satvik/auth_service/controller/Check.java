package com.satvik.auth_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Check {

    private static final Logger logger = LoggerFactory.getLogger(Check.class);

    @GetMapping("/check")
    public String check() {
        logger.info("Entered check controller ");
        logger.info("checked successful ");
        return "check success!";
    }
}
