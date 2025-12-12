package com.danielgofu.adminpanel.controller;

import com.danielgofu.adminpanel.dto.*;
import com.danielgofu.adminpanel.entity.User;
import com.danielgofu.adminpanel.service.AuthService;
import com.danielgofu.adminpanel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterRequest req) {
        User u = authService.register(req);
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setRole(u.getRole());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        var tokens = authService.login(req);
        return ResponseEntity.ok(new AuthResponse(tokens.accessToken, tokens.refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest rreq) {
        try {
            String newAccess = authService.refreshAccessToken(rreq.getRefreshToken());
            return ResponseEntity.ok(new AccessTokenResponse(newAccess));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequest rreq){
        authService.logout(rreq.getRefreshToken());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        var userOpt = userService.findByUsername(principal.getUsername());
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();
        var u = userOpt.get();
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setRole(u.getRole());
        return ResponseEntity.ok(dto);
    }
}
