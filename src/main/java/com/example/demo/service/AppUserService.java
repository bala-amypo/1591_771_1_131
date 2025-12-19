package com.example.demo.service;

import com.example.demo.entity.AppUser;

import java.util.List;

public interface AppUserService {

    AppUser registerUser(AppUser user);

    AppUser getUserById(Long id);

    List<AppUser> getAllUsers();

    AppUser deactivateUser(Long id);

    AppUser getByEmail(String email);

    AppUser register(String email, String password, String role);

    AppUser login(String email, String password);
}
