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
        
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email must be unique"); 
        }

     
        AppUser user = new AppUser();
        user.setEmail(email);
        user.setPassword(password); 
        user.setRole(role);
        user.setActive(true);

        return userRepository.save(user); 
    }

    @Override
    public String login(String email, String password) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid credentials")); 

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid credentials");
        }
        
        return "Login successful for " + email;
    }
}