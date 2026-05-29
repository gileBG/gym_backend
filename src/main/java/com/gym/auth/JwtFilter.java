package com.gym.auth;

import com.gym.user.UserRepository;
import com.gym.user.VezbacRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final VezbacRepository vezbacRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String email = jwtUtil.extractEmail(token);
        final String tokenRole = jwtUtil.extractRole(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String effectiveRole = tokenRole;

            if (effectiveRole == null || effectiveRole.isBlank()) {
                effectiveRole = resolveRoleByEmail(email);
            }

            effectiveRole = normalizeRole(effectiveRole);

            if (effectiveRole != null && !effectiveRole.isBlank()) {
                var auth = new UsernamePasswordAuthenticationToken(
                        email, null, List.of(new SimpleGrantedAuthority("ROLE_" + effectiveRole))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveRoleByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getRola().name())
                .or(() -> vezbacRepository.findByEmail(email).map(vezbac -> "VEZBAC"))
                .orElse(null);
    }

    private String normalizeRole(String role) {
        if (role == null) {
            return null;
        }

        String normalized = role.trim().toUpperCase(Locale.ROOT);
        if (normalized.startsWith("ROLE_")) {
            normalized = normalized.substring(5);
        }

        return normalized;
    }
}
