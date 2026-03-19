package com.wanshi.library.unit.security;
import com.wanshi.library.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void shouldGenerateAccessToken() {
        String token = jwtUtil.generateAccessToken("admin", "ADMIN");

        assertNotNull(token);
        assertEquals("admin", jwtUtil.extractUsername(token));
        assertEquals("ADMIN", jwtUtil.extractRole(token));
        assertEquals("access", jwtUtil.extractType(token));
    }

    @Test
    void shouldGenerateRefreshToken() {
        String token = jwtUtil.generateRefreshToken("admin");

        assertNotNull(token);
        assertEquals("admin", jwtUtil.extractUsername(token));
        assertEquals("refresh", jwtUtil.extractType(token));
    }

    @Test
    void accessTokenShouldNotBeExpiredImmediately() {
        String token = jwtUtil.generateAccessToken("admin", "ADMIN");

        assertFalse(jwtUtil.isTokenExpired(token));
    }
}