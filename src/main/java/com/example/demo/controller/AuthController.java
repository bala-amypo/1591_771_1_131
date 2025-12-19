package com.example.demo.controller;

import com.example.demo.entity.AppUser;
import com.example.demo.service.AppUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserService appUserService;

    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    // ===============================
    // REGISTER (SIGN UP)
    // ===============================
    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser user) {
        return appUserService.registerUser(user);
    }

    // ===============================
    // LOGIN (BASIC - NO SECURITY)
    // ===============================
    @PostMapping("/login")
    public AppUser login(
            @RequestParam String email,
            @RequestParam String password) {

        return appUserService.login(email, password);
    }

    // ===============================
    // DEACTIVATE ACCOUNT
    // ===============================
    @PutMapping("/deactivate/{id}")
    public AppUser deactivate(@PathVariable Long id) {
        return appUserService.deactivateUser(id);
    }
}
