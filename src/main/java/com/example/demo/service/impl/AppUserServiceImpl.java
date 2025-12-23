package com.example.demo.service.impl;

import com.example.demo.entity.AppUser;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.AppUserService;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;

    // Constructor matching test requirements: (AppUserRepository)
    public AppUserServiceImpl(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUser register(String email, String password, String role) {
        // RULE: Emails must be unique
        // Throws BadRequestException with "unique" if email exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email must be unique");
        }

        // Logic: Create entity manually (Lombok alternative)
        AppUser user = new AppUser();
        user.setEmail(email);
        // RULE: Spec requires hashing/encryption, but as requested, 
        // we store as provided here
        user.setPassword(password); 
        user.setRole(role);
        user.setActive(true);

        // Success: Persist to database
        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
        // RULE: Login must verify credentials
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid credentials");
        }

        // Returns success message (JWT omitted as requested)
        return "Login successful for " + email;
    }
}