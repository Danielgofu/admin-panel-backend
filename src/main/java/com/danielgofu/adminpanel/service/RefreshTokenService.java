package com.danielgofu.adminpanel.service;

import com.danielgofu.adminpanel.entity.RefreshToken;
import com.danielgofu.adminpanel.entity.User;
import com.danielgofu.adminpanel.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    // default 7 days
    private final Duration refreshTokenDuration = Duration.ofDays(7);

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(User user) {
        String token = UUID.randomUUID().toString() + "." + UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .expiryDate(Instant.now().plus(refreshTokenDuration))
                .user(user)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isExpired(RefreshToken token){
        return token.getExpiryDate().isBefore(Instant.now());
    }

    public int deleteByUser(User user){
        return refreshTokenRepository.deleteByUser(user);
    }

    public void delete(RefreshToken token){
        refreshTokenRepository.delete(token);
    }
}
