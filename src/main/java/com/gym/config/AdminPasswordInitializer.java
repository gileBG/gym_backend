package com.gym.config;

import com.gym.user.Role;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AdminPasswordInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner resetAdminPasswordOnStartup() {
        return args -> {
            List<User> adminUsers = userRepository.findAllByRola(Role.ADMIN);

            if (adminUsers.isEmpty()) {
                User admin = new User();
                admin.setIme("Admin");
                admin.setPrezime("Sistem");
                admin.setEmail("admin@gym.local");
                admin.setLozinka(passwordEncoder.encode("admin123"));
                admin.setRola(Role.ADMIN);
                admin.setDatumRegistracije(LocalDateTime.now());
                admin.setTemp(0);
                userRepository.save(admin);
                return;
            }

            String encodedPassword = passwordEncoder.encode("admin123");
            for (User admin : adminUsers) {
                admin.setLozinka(encodedPassword);
            }
            userRepository.saveAll(adminUsers);
        };
    }
}
