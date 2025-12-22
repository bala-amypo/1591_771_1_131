package com.example.demo.service.impl;

import com.example.demo.entity.AppUser;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.AppUserService;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;

    public AppUserServiceImpl(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUser register(String email, String password, String role) {
        // Enforce uniqueness
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("email must be unique");
        }

        AppUser newUser = AppUser.builder()
                .email(email)
                .password(password) // In real security, use passwordEncoder.encode(password)
                .role(role)
                .active(true)
                .build();

        return userRepository.save(newUser);
    }

    @Override
    public String login(String email, String password) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid credentials");
        }

        // Placeholder for JWT token generation
        return "dummy-jwt-token-for-" + email;
    }
}