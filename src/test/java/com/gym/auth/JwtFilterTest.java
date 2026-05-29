package com.gym.auth;

import com.gym.user.UserRepository;
import com.gym.user.Vezbac;
import com.gym.user.VezbacRepository;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    private static final String SECRET = "ovaj-tajni-kljuc-mora-biti-dugacak-najmanje-256-bita!";
    private static final long EXPIRATION = 86400000L;

    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VezbacRepository vezbacRepository;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(SECRET);
        jwtProperties.setExpiration(EXPIRATION);
        jwtUtil = new JwtUtil(jwtProperties);
        jwtFilter = new JwtFilter(jwtUtil, userRepository, vezbacRepository);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_vezbacWithoutRoleClaim_assignsVezbacRole() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String token = jwtUtil.generateToken("vezbac@test.com", null);
        request.addHeader("Authorization", "Bearer " + token);

        Vezbac vezbac = new Vezbac();
        vezbac.setEmail("vezbac@test.com");

        when(userRepository.findByEmail("vezbac@test.com")).thenReturn(Optional.empty());
        when(vezbacRepository.findByEmail("vezbac@test.com")).thenReturn(Optional.of(vezbac));

        jwtFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_VEZBAC");
        verify(filterChain).doFilter(request, response);
    }
}