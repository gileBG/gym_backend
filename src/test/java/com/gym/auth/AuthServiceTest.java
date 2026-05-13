package com.gym.auth;

import com.gym.user.Role;
import com.gym.user.User;
import com.gym.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setIme("Petar");
        user.setPrezime("Petrović");
        user.setEmail("petar@test.com");
        user.setLozinka("hash123");
        user.setRola(Role.FRONT_DESK);
    }

    @Test
    void register_uspesno() {
        RegisterRequest request = new RegisterRequest();
        request.setIme("Petar");
        request.setPrezime("Petrović");
        request.setEmail("petar@test.com");
        request.setLozinka("lozinka");
        request.setRola(Role.FRONT_DESK);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getLozinka())).thenReturn("hash123");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(user.getEmail(), Role.FRONT_DESK.name())).thenReturn("token123");

        LoginResponse response = authService.register(request);

        assertThat(response.getToken()).isEqualTo("token123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_emailPostoji_bacaIzuzetak() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("petar@test.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email već postoji");
    }

    @Test
    void register_rolaNijeProsledjena_bacaIzuzetak() {
        RegisterRequest request = new RegisterRequest();
        request.setIme("Petar");
        request.setPrezime("Petrović");
        request.setEmail("petar@test.com");
        request.setLozinka("lozinka");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rola je obavezna");
    }

    @Test
    void login_uspesno() {
        LoginRequest request = new LoginRequest();
        request.setEmail("petar@test.com");
        request.setLozinka("lozinka");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getLozinka(), user.getLozinka())).thenReturn(true);
        when(jwtUtil.generateToken(user.getEmail(), Role.FRONT_DESK.name())).thenReturn("token123");

        LoginResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("token123");
    }

    @Test
    void login_pogresnaLozinka_bacaIzuzetak() {
        LoginRequest request = new LoginRequest();
        request.setEmail("petar@test.com");
        request.setLozinka("pogresna");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getLozinka(), user.getLozinka())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void login_zaposleniNijePronasjen_bacaIzuzetak() {
        LoginRequest request = new LoginRequest();
        request.setEmail("nepostoji@test.com");
        request.setLozinka("lozinka");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
