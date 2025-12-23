package com.example.demo.controller;
import com.example.demo.entity.AppUser;
import com.example.demo.service.impl.AppUserServiceImpl;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    private final AppUserServiceImpl userService;

    public AuthController(AppUserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public AppUser register(@RequestBody Map<String, String> request) {
        return userService.register(
            request.get("email"), 
            request.get("password"), 
            request.get("role")
        );
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> request) {
        return userService.login(request.get("email"), request.get("password"));
    }
}