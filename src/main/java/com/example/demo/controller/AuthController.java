package com.example.demo.controller;

import com.example.demo.entity.AppUser;
import com.example.demo.service.AppUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    private final AppUserService userService;

    public AuthController(AppUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser user) {
        return userService.register(
            user.getEmail(), 
            user.getPassword(), 
            user.getRole()
        );
    }

    @PostMapping("/login")
    public String login(@RequestBody AppUser user) {
        return userService.login(user.getEmail(), user.getPassword());
    }
}