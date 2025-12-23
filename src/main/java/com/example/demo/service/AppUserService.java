package com.example.demo.service;

import com.example.demo.entity.AppUser;

public interface AppUserService {
    // Required by spec: register with email, password, role
    AppUser register(String email, String password, String role);
    
    // Required by spec: login returning a string
    String login(String email, String password);
}