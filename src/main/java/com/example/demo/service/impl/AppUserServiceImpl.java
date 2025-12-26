package com.example.demo.service.impl;

import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.AppUser;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AppUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {
    
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    public AppUserServiceImpl(AppUserRepository appUserRepository, 
                              PasswordEncoder passwordEncoder,
                              JwtTokenProvider jwtTokenProvider) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AppUser register(String email, String password, String role) {
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("Email cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Password cannot be null or empty");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new BadRequestException("Role cannot be null or empty");
        }
        
        if (appUserRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email must be unique");
        }
        
        AppUser user = AppUser.builder()
                .email(email.trim())
                .password(passwordEncoder.encode(password))
                .role(role.trim())
                .active(true)
                .build();
        
        return appUserRepository.save(user);
    }

    @Override
    public AuthResponse login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("Email cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Password cannot be null or empty");
        }
        
        AppUser user = appUserRepository.findByEmail(email.trim())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        
        String token = jwtTokenProvider.createToken(user);
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("Failed to generate token");
        }
        
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}