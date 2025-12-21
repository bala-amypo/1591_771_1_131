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

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtProvider;

    public AppUserServiceImpl(AppUserRepository repo,
                              PasswordEncoder encoder,
                              JwtTokenProvider jwtProvider) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void register(String email, String password, String role) {

        repo.findByEmail(email).ifPresent(u -> {
            throw new BadRequestException("Email must be unique");
        });

        AppUser user = AppUser.builder()
                .email(email)
                .password(encoder.encode(password))
                .role(role)
                .active(true)
                .build();

        repo.save(user);
    }

    @Override
    public AuthResponse login(String email, String password) {

        AppUser user = repo.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = jwtProvider.createToken(user);

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}
