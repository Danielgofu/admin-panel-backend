package com.danielgofu.adminpanel.repository;

import com.danielgofu.adminpanel.entity.RefreshToken;
import com.danielgofu.adminpanel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    int deleteByUser(User user);
}
