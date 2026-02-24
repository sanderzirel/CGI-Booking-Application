package com.example.backend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    @GetMapping
    public String getTables() {
        return "Tables API works!";
    }
}
