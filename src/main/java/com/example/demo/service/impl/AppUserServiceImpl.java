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
        // Validation for uniqueness
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email must be unique");
        }
        
        // This line [25,24] will now work because of the constructor added above
        AppUser user = new AppUser(email, password, role);
        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid credentials");
        }
        return "Login successful";
    }
}