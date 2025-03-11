package com.expense.tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthChecks {

    @GetMapping("/healthCheck")
    public String healthCheckOk() {
        return "OK";
    }
}
