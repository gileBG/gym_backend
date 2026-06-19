package com.gym.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profil")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final VezbacRepository vezbacRepository;
    private final AvatarStorageService avatarStorageService;

    @PostMapping("/avatar")
    @Transactional
    public ResponseEntity<AvatarUploadResponse> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String email = currentEmail();
        String avatarUrl = avatarStorageService.store(file);

        return userRepository.findByEmail(email)
                .map(user -> {
                    String previous = user.getAvatarUrl();
                    user.setAvatarUrl(avatarUrl);
                    userRepository.save(user);
                    avatarStorageService.deleteIfManaged(previous);
                    return ResponseEntity.ok(new AvatarUploadResponse(user.getId(), user.getRola().name(), avatarUrl));
                })
                .or(() -> vezbacRepository.findByEmail(email)
                        .map(vezbac -> {
                            String previous = vezbac.getAvatarUrl();
                            vezbac.setAvatarUrl(avatarUrl);
                            vezbacRepository.save(vezbac);
                            avatarStorageService.deleteIfManaged(previous);
                            return ResponseEntity.ok(new AvatarUploadResponse(vezbac.getId(), "VEZBAC", avatarUrl));
                        }))
                .orElseThrow(() -> new IllegalArgumentException("Ulogovani korisnik nije pronađen."));
    }

    @PostMapping("/avatar/vezbaci/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<AvatarUploadResponse> uploadAvatarForVezbac(@PathVariable Long id,
                                                                       @RequestParam("file") MultipartFile file) {
        String avatarUrl = avatarStorageService.store(file);

        Vezbac vezbac = vezbacRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik nije pronađen."));

        String previous = vezbac.getAvatarUrl();
        vezbac.setAvatarUrl(avatarUrl);
        vezbacRepository.save(vezbac);
        avatarStorageService.deleteIfManaged(previous);

        return ResponseEntity.ok(new AvatarUploadResponse(vezbac.getId(), "VEZBAC", avatarUrl));
    }

    @PostMapping("/avatar/zaposleni/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<AvatarUploadResponse> uploadAvatarForZaposleni(@PathVariable Long id,
                                                                          @RequestParam("file") MultipartFile file) {
        String avatarUrl = avatarStorageService.store(file);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik nije pronađen."));

        String previous = user.getAvatarUrl();
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
        avatarStorageService.deleteIfManaged(previous);

        return ResponseEntity.ok(new AvatarUploadResponse(user.getId(), user.getRola().name(), avatarUrl));
    }

    @DeleteMapping("/avatar/vezbaci/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<AvatarUploadResponse> deleteAvatarForVezbac(@PathVariable Long id) {
        Vezbac vezbac = vezbacRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik nije pronađen."));

        String previous = vezbac.getAvatarUrl();
        vezbac.setAvatarUrl(null);
        vezbacRepository.save(vezbac);
        avatarStorageService.deleteIfManaged(previous);

        return ResponseEntity.ok(new AvatarUploadResponse(vezbac.getId(), "VEZBAC", null));
    }

    @DeleteMapping("/avatar/zaposleni/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<AvatarUploadResponse> deleteAvatarForZaposleni(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik nije pronađen."));

        String previous = user.getAvatarUrl();
        user.setAvatarUrl(null);
        userRepository.save(user);
        avatarStorageService.deleteIfManaged(previous);

        return ResponseEntity.ok(new AvatarUploadResponse(user.getId(), user.getRola().name(), null));
    }

    private String currentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new IllegalArgumentException("Nije moguće odrediti korisnika iz sesije.");
        }
        return authentication.getName();
    }
}