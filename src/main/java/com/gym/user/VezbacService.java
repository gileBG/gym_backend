package com.gym.user;

import com.gym.membership.TipClanarine;
import com.gym.notification.AccountNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VezbacService {

    private final VezbacRepository vezbacRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountNotificationService accountNotificationService;

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER', 'TRENER')")
    public List<Vezbac> getAll() {
        return vezbacRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER', 'TRENER')")
    public Vezbac getById(Long id) {
        return vezbacRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vezbac nije pronađen."));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER', 'TRENER')")
    public Vezbac getByZaposleniId(Long zaposleniId) {
        return getById(zaposleniId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER')")
    public Vezbac create(VezbacRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Telo zahteva je obavezno.");
        }
        if (request.getIme() == null || request.getIme().isBlank()) {
            throw new IllegalArgumentException("Polje ime je obavezno.");
        }
        if (request.getPrezime() == null || request.getPrezime().isBlank()) {
            throw new IllegalArgumentException("Polje prezime je obavezno.");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Polje email je obavezno.");
        }
        if (request.getLozinka() == null || request.getLozinka().isBlank()) {
            throw new IllegalArgumentException("Polje lozinka je obavezno.");
        }

        if (vezbacRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email vežbača već postoji.");
        }

        Vezbac vezbac = new Vezbac();
        vezbac.setIme(request.getIme());
        vezbac.setPrezime(request.getPrezime());
        vezbac.setAdresa(request.getAdresa());
        vezbac.setEmail(request.getEmail());
        vezbac.setLozinka(passwordEncoder.encode(request.getLozinka()));
        vezbac.setTipClanarine(request.getTipClanarine() != null ? request.getTipClanarine() : TipClanarine.STANDARD);

        Vezbac savedVezbac = vezbacRepository.save(vezbac);

        accountNotificationService.sendNewAccountNotification(
            savedVezbac.getIme() + " " + savedVezbac.getPrezime(),
            savedVezbac.getEmail(),
            "VEZBAC"
        );

        return savedVezbac;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER')")
    public Vezbac update(Long id, VezbacRequest request) {
        Vezbac vezbac = vezbacRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vezbac nije pronađen."));

        if (request.getIme() != null) {
            vezbac.setIme(request.getIme());
        }
        if (request.getPrezime() != null) {
            vezbac.setPrezime(request.getPrezime());
        }
        if (request.getAdresa() != null) {
            vezbac.setAdresa(request.getAdresa());
        }
        if (request.getEmail() != null) {
            vezbacRepository.findByEmail(request.getEmail())
                    .filter(postojeci -> !postojeci.getId().equals(vezbac.getId()))
                    .ifPresent(postojeci -> {
                        throw new IllegalArgumentException("Email vežbača već postoji.");
                    });
            vezbac.setEmail(request.getEmail());
        }
        if (request.getLozinka() != null && !request.getLozinka().isBlank()) {
            vezbac.setLozinka(passwordEncoder.encode(request.getLozinka()));
        }
        if (request.getTipClanarine() != null) {
            vezbac.setTipClanarine(request.getTipClanarine());
        }

        return vezbacRepository.save(vezbac);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        if (!vezbacRepository.existsById(id)) {
            throw new IllegalArgumentException("Vezbac nije pronađen.");
        }
        vezbacRepository.deleteById(id);
    }
}
