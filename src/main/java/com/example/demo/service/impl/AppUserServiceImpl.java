package com.example.demo.service.impl;
import com.example.demo.entity.AppUser;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl {
    private final AppUserRepository userRepository;

    public AppUserServiceImpl(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser register(String email, String password, String role) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email must be unique");
        }
        // In a real app, password would be encrypted here
        AppUser user = new AppUser(email, password, role);
        return userRepository.save(user);
    }

    public String login(String email, String password) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid credentials");
        }
        return "Login successful for " + email;
    }
}