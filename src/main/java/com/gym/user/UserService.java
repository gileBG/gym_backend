package com.gym.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER', 'TRENER')")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User create(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email već postoji.");
        }

        if (request.getRola() == null) {
            throw new IllegalArgumentException("Rola je obavezna.");
        }

        User user = new User();
        user.setIme(request.getIme());
        user.setPrezime(request.getPrezime());
        user.setEmail(request.getEmail());
        user.setLozinka(passwordEncoder.encode(request.getLozinka()));
        user.setRola(request.getRola());
        user.setTemp(0);

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zaposleni nije pronađen."));
    }
}
