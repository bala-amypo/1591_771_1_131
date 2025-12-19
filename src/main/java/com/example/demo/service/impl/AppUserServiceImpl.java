package com.example.demo.service.impl;

import com.example.demo.entity.AppUser;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.AppUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;

    public AppUserServiceImpl(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ===============================
    // REGISTER USER
    // ===============================
    @Override
    public AppUser registerUser(AppUser user) {
        user.setActive(true);
        return userRepository.save(user);
    }

    // ===============================
    // LOGIN (NO JWT)
    // ===============================
    @Override
    public AppUser login(String email, String password) {

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("User is deactivated");
        }

        return user;
    }

    // ===============================
    // GET USER BY ID
    // ===============================
    @Override
    public AppUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ===============================
    // GET ALL USERS
    // ===============================
    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    // ===============================
    // DEACTIVATE USER
    // ===============================
    @Override
    public AppUser deactivateUser(Long id) {
        AppUser user = getUserById(id);
        user.setActive(false);
        return userRepository.save(user);
    }

    @Override
    public AppUser getByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByEmail'");
    }

    @Override
    public AppUser register(String email, String password, String role) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }
}
