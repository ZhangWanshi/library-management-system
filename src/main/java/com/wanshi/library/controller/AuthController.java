package com.wanshi.library.controller;

import com.wanshi.library.dto.RefreshRequest;
import com.wanshi.library.entity.User;
import com.wanshi.library.exception.InvalidRefreshTokenException;
import com.wanshi.library.repository.UserRepository;
import com.wanshi.library.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String accessToken = jwtUtil.generateAccessToken(
                user.getUsername(),
                user.getRole().getName()
        );

        String refreshToken = jwtUtil.generateRefreshToken(
                user.getUsername()
        );

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "role", user.getRole().getName()
        );
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody RefreshRequest request) {

        String refreshToken = request.getRefreshToken();

        if (jwtUtil.isTokenExpired(refreshToken) ||
                !"refresh".equals(jwtUtil.extractType(refreshToken))) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        String newAccessToken = jwtUtil.generateAccessToken(
                user.getUsername(),
                user.getRole().getName()
        );

        return Map.of("accessToken", newAccessToken);
    }
}