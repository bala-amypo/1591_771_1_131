package com.example.demo.controller;

import com.example.demo.entity.AppUser;
import com.example.demo.service.impl.AppUserServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    private final AppUserServiceImpl userService;

    public AuthController(AppUserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }
}