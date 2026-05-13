package com.gym.config;

import com.gym.user.Role;
import com.gym.user.User;
import com.gym.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminPasswordInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<Iterable<User>> saveAllCaptor;

    @InjectMocks
    private AdminPasswordInitializer adminPasswordInitializer;

    @Test
    void resetAdminPasswordOnStartup_resetujeAdminLozinku() throws Exception {
        User existingAdmin = new User();
        existingAdmin.setEmail("postojeci.admin@gym.com");
        existingAdmin.setLozinka("old-hash");
        existingAdmin.setRola(Role.ADMIN);

        when(passwordEncoder.encode("admin123")).thenReturn("encoded-admin123");
        when(userRepository.findAllByRola(Role.ADMIN)).thenReturn(List.of(existingAdmin));

        ApplicationRunner runner = adminPasswordInitializer.resetAdminPasswordOnStartup();
        runner.run(null);

        verify(userRepository, times(1)).saveAll(saveAllCaptor.capture());

        List<User> updatedAdmins = toList(saveAllCaptor.getValue());
        assertThat(updatedAdmins).singleElement().extracting(User::getLozinka).isEqualTo("encoded-admin123");
    }

    @Test
    void resetAdminPasswordOnStartup_neCuvaNistaKadaSveVecPostoji() throws Exception {
        when(userRepository.findAllByRola(Role.ADMIN)).thenReturn(List.of());

        ApplicationRunner runner = adminPasswordInitializer.resetAdminPasswordOnStartup();
        runner.run(null);

        verify(userRepository, never()).saveAll(org.mockito.ArgumentMatchers.any());
    }

    private List<User> toList(Iterable<User> users) {
        List<User> userList = new ArrayList<>();
        for (User user : users) {
            userList.add(user);
        }
        return userList;
    }
}