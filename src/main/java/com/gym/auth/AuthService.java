package com.gym.auth;

import com.gym.notification.AccountNotificationService;
import com.gym.user.Vezbac;
import com.gym.user.VezbacRepository;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final VezbacRepository vezbacRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AccountNotificationService accountNotificationService;

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email već postoji.");
        }

        User user = new User();
        user.setIme(request.getIme());
        user.setPrezime(request.getPrezime());
        user.setEmail(request.getEmail());
        user.setLozinka(passwordEncoder.encode(request.getLozinka()));
        if (request.getRola() == null) {
            throw new IllegalArgumentException("Rola je obavezna.");
        }

        user.setRola(request.getRola());
        User savedUser = userRepository.save(user);

        accountNotificationService.sendNewAccountNotification(
            savedUser.getIme() + " " + savedUser.getPrezime(),
            savedUser.getEmail(),
            savedUser.getRola().name()
        );

        return new LoginResponse(
            jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRola().name()),
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getIme(),
            savedUser.getPrezime(),
            savedUser.getRola().name(),
            savedUser.getAvatarUrl()
        );
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user != null) {
            if (!passwordEncoder.matches(request.getLozinka(), user.getLozinka())) {
                throw new IllegalArgumentException("Pogrešan email ili lozinka.");
            }

            return new LoginResponse(
                    jwtUtil.generateToken(user.getEmail(), user.getRola().name()),
                    user.getId(),
                    user.getEmail(),
                    user.getIme(),
                    user.getPrezime(),
                    user.getRola().name(),
                    user.getAvatarUrl()
            );
        }

        Vezbac vezbac = vezbacRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Pogrešan email ili lozinka."));

        if (!passwordEncoder.matches(request.getLozinka(), vezbac.getLozinka())) {
            throw new IllegalArgumentException("Pogrešan email ili lozinka.");
        }

        return new LoginResponse(
                jwtUtil.generateToken(vezbac.getEmail(), "VEZBAC"),
                vezbac.getId(),
                vezbac.getEmail(),
                vezbac.getIme(),
                vezbac.getPrezime(),
            "VEZBAC",
            vezbac.getAvatarUrl()
        );
    }
}
