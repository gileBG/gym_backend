package com.gym.auth;

// import com.gym.user.Role;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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
        userRepository.save(user);

        return new LoginResponse(
                jwtUtil.generateToken(user.getEmail(), user.getRola().name()),
                user.getId(),
                user.getEmail(),
                user.getIme(),
                user.getPrezime(),
                user.getRola().name()
        );
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Pogrešan email ili lozinka."));

        if (!passwordEncoder.matches(request.getLozinka(), user.getLozinka())) {
            throw new IllegalArgumentException("Pogrešan email ili lozinka.");
        }

        return new LoginResponse(
                jwtUtil.generateToken(user.getEmail(), user.getRola().name()),
                user.getId(),
                user.getEmail(),
                user.getIme(),
                user.getPrezime(),
                user.getRola().name()
        );
    }
}
