package com.gym.auth;

import com.gym.notification.AccountNotificationService;
import com.gym.user.Role;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.Vezbac;
import com.gym.user.VezbacRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String SECRET = "ovaj-tajni-kljuc-mora-biti-dugacak-najmanje-256-bita!";
    private static final long EXPIRATION = 86400000L;

    @Mock
    private UserRepository userRepository;
    @Mock
    private VezbacRepository vezbacRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    @Mock
    private AccountNotificationService accountNotificationService;

    private AuthService authService;

    private User user;
    private Vezbac vezbac;

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(SECRET);
        jwtProperties.setExpiration(EXPIRATION);
        jwtUtil = new JwtUtil(jwtProperties);

        authService = new AuthService(
            userRepository,
            vezbacRepository,
            passwordEncoder,
            jwtUtil,
            accountNotificationService
        );

        user = new User();
        user.setId(1L);
        user.setIme("Petar");
        user.setPrezime("Petrović");
        user.setEmail("petar@test.com");
        user.setLozinka("hash123");
        user.setRola(Role.FRONT_DESK);

        vezbac = new Vezbac();
        vezbac.setId(2L);
        vezbac.setIme("Marko");
        vezbac.setPrezime("Petrovic");
        vezbac.setEmail("marko@test.com");
        vezbac.setLozinka("hash456");
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

        LoginResponse response = authService.register(request);

        assertThat(response.getToken()).isNotBlank();
        verify(userRepository).save(any(User.class));
        verify(accountNotificationService).sendNewAccountNotification(
            "Petar Petrović",
            "petar@test.com",
            Role.FRONT_DESK.name()
        );
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

        LoginResponse response = authService.login(request);

        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getRola()).isEqualTo(Role.FRONT_DESK.name());
    }

    @Test
    void login_vezbac_uspesno() {
        LoginRequest request = new LoginRequest();
        request.setEmail("marko@test.com");
        request.setLozinka("lozinka");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(vezbacRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(vezbac));
        when(passwordEncoder.matches(request.getLozinka(), vezbac.getLozinka())).thenReturn(true);

        LoginResponse response = authService.login(request);

        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getRola()).isEqualTo("VEZBAC");
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
    void login_vezbac_pogresnaLozinka_bacaIzuzetak() {
        LoginRequest request = new LoginRequest();
        request.setEmail("marko@test.com");
        request.setLozinka("pogresna");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(vezbacRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(vezbac));
        when(passwordEncoder.matches(request.getLozinka(), vezbac.getLozinka())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void login_zaposleniNijePronasjen_bacaIzuzetak() {
        LoginRequest request = new LoginRequest();
        request.setEmail("nepostoji@test.com");
        request.setLozinka("lozinka");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(vezbacRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
