package com.gym.membership;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClanarinaService {

    private final ClanarinaRepository clanarinaRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public Clanarina create(ClanarinaRequest request) {
        if (request.getNaziv() == null || request.getNaziv().isBlank()) {
            throw new IllegalArgumentException("Naziv je obavezan.");
        }
        if (request.getCena() == null) {
            throw new IllegalArgumentException("Cena je obavezna.");
        }
        if (request.getTipClanarine() == null) {
            throw new IllegalArgumentException("Tip clanarine je obavezan.");
        }

        Clanarina clanarina = new Clanarina();
        clanarina.setNaziv(request.getNaziv().trim());
        clanarina.setCena(request.getCena());
        clanarina.setTipClanarine(request.getTipClanarine());

        return clanarinaRepository.save(clanarina);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Clanarina> getAll() {
        return clanarinaRepository.findAll();
    }
}
