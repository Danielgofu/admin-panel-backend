package com.danielgofu.adminpanel.service;

import com.danielgofu.adminpanel.config.JwtUtils;
import com.danielgofu.adminpanel.dto.LoginRequest;
import com.danielgofu.adminpanel.dto.RegisterRequest;
import com.danielgofu.adminpanel.entity.RefreshToken;
import com.danielgofu.adminpanel.entity.User;
import com.danielgofu.adminpanel.repository.UserRepository;
import com.danielgofu.adminpanel.util.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public User register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        String role = req.getRole();
        if (role == null || role.isBlank()) role = Role.USER.name();
        if (!role.equals(Role.USER.name()) && !role.equals(Role.ADMIN.name())) {
            role = Role.USER.name();
        }
        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .build();
        return userRepository.save(user);
    }

    public AuthTokens login(LoginRequest req) {
        Optional<User> uOpt = userRepository.findByUsername(req.getUsername());
        if (uOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        User u = uOpt.get();
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String access = jwtUtils.generateAccessToken(u.getUsername(), u.getRole());
        RefreshToken r = refreshTokenService.createRefreshToken(u);
        return new AuthTokens(access, r.getToken());
    }

    public static class AuthTokens {
        public final String accessToken;
        public final String refreshToken;
        public AuthTokens(String accessToken, String refreshToken){
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    public String refreshAccessToken(String refreshToken) {
        var tokenOpt = refreshTokenService.findByToken(refreshToken);
        if (tokenOpt.isEmpty()) {
            throw new IllegalArgumentException("Refresh token not found");
        }
        RefreshToken token = tokenOpt.get();
        if (refreshTokenService.isExpired(token)) {
            refreshTokenService.delete(token);
            throw new IllegalArgumentException("Refresh token expired");
        }
        User user = token.getUser();
        return jwtUtils.generateAccessToken(user.getUsername(), user.getRole());
    }

    public void logout(String refreshToken) {
        var tokenOpt = refreshTokenService.findByToken(refreshToken);
        tokenOpt.ifPresent(refreshTokenService::delete);
    }
}
