package com.example.recetas_back.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class JWTUtilTest {

    private JWTUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JWTUtil(); // Instanciar el JWTUtil
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("testUser", "testRole");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken("testUser", "testRole");

        String username = jwtUtil.extractUsername(token);

        assertEquals("testUser", username);
    }

    @Test
    void testValidateTokenValid() {
        String token = jwtUtil.generateToken("testUser", "testRole");

        boolean isValid = jwtUtil.validateToken(token, "testUser");

        assertTrue(isValid);
    }

    @Test
    void testValidateTokenInvalidUsername() {
        String token = jwtUtil.generateToken("testUser", "testRole");

        boolean isValid = jwtUtil.validateToken(token, "wrongUser");

        assertFalse(isValid);
    }
}
