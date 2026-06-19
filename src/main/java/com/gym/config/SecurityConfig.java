package com.gym.config;




import com.gym.auth.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/roles").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/zaposleni", "/api/zaposleni/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/programi", "/api/programi/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/clanarine-cenovnik", "/api/clanarine-cenovnik/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/clanarine", "/api/clanarine/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/zaposleni").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/zaposleni/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/programi", "/api/clanarine", "/api/clanarine-cenovnik").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/profil/avatar").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/profil/avatar/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/profil/avatar/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/programi/**", "/api/clanarine-cenovnik/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/uplate-programi").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/uplate-programi/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/uplate-programi/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/api/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/programi/**", "/api/clanarine-cenovnik/**").hasRole("ADMIN")
                        .requestMatchers("/", "/h2-console/**", "/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
            "http://localhost:*",
            "http://127.0.0.1:*",
            "https://localhost:*",
            "https://127.0.0.1:*"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
