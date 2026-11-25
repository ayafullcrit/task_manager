package com.phong.testsec.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    // Endpoint 1: Public — ai cũng gọi được
    @GetMapping("/public")
    public String publicEndpoint() {
        return "Endpoint này PUBLIC — ai cũng vào được";
    }

    // Endpoint 2: Cần đăng nhập
    @GetMapping("/private")
    public String privateEndpoint() {
        return "Endpoint này PRIVATE — phải đăng nhập";
    }

    // Endpoint 3: Chỉ ADMIN mới vào được
    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Endpoint này chỉ dành cho ADMIN";
    }
}
