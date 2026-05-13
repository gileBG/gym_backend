package com.gym.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class JwtUtilTest {

    private static final String SECRET = "ovaj-tajni-kljuc-mora-biti-dugacak-najmanje-256-bita!";
    private static final long EXPIRATION = 86400000L;

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(SECRET);
        jwtProperties.setExpiration(EXPIRATION);
        jwtUtil = new JwtUtil(jwtProperties);
    }

    @Test
    void generateToken_extractEmail_matchuju() {
        String token = jwtUtil.generateToken("petar@test.com", "FRONT_DESK");
        assertThat(jwtUtil.extractEmail(token)).isEqualTo("petar@test.com");
    }

    @Test
    void generateToken_extractRole_matchuju() {
        String token = jwtUtil.generateToken("petar@test.com", "ADMIN");
        assertThat(jwtUtil.extractRole(token)).isEqualTo("ADMIN");
    }

    @Test
    void isTokenValid_validanToken_vraceTrue() {
        String token = jwtUtil.generateToken("petar@test.com", "FRONT_DESK");
        assertThat(jwtUtil.isTokenValid(token)).isTrue();
    }

    @Test
    void isTokenValid_nevalidanToken_vraceFalse() {
        assertThat(jwtUtil.isTokenValid("nevalidan.token.string")).isFalse();
    }
}
