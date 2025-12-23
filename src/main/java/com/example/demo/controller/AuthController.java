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
        // Extracting fields from the entity to match the Service method signature
        return userService.register(
            user.getEmail(), 
            user.getPassword(), 
            user.getRole()
        );
    }

    @PostMapping("/login")
    public String login(@RequestBody AppUser user) {
        // Only email and password are used for login logic
        return userService.login(user.getEmail(), user.getPassword());
    }
}