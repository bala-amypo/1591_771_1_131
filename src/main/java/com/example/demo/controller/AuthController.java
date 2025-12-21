package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.service.AppUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AppUserService appUserService;

    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    // 🔹 Register new user
    @PostMapping("/register")
    public void register(@RequestBody AuthRequest request) {
        appUserService.register(
                request.getEmail(),
                request.getPassword(),
                "USER"
        );
    }

    // 🔹 Login user and return JWT
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return appUserService.login(
                request.getEmail(),
                request.getPassword()
        );
    }
}
