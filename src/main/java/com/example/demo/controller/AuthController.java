package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.AppUser;
import com.example.demo.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AppUserService appUserService;
    
    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody AuthRequest request) {
        AppUser user = appUserService.register(request.getEmail(), request.getPassword(), "USER");
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = appUserService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}