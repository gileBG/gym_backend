package com.gym.membership;

import com.gym.user.VezbacRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final VezbacRepository vezbacRepository;
    private final ClanarinaRepository clanarinaRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public Membership create(MembershipRequest request) {
        Membership membership = new Membership();
        applyRequestData(membership, request);
        return membershipRepository.save(membership);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER')")
    public List<Membership> getAll() {
        return membershipRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER', 'TRENER', 'VEZBAC')")
    public Membership getById(Long id) {
        return membershipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Uplata nije pronađena."));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER', 'TRENER', 'VEZBAC')")
    public List<Membership> getByVezbac(Long vezbacId) {
        return membershipRepository.findByVezbacId(vezbacId);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER', 'TRENER', 'VEZBAC')")
    public List<Membership> getByClanarina(Long clanarinaId) {
        return membershipRepository.findByClanarinaId(clanarinaId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Membership update(Long id, MembershipRequest request) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Uplata nije pronađena."));
        applyRequestData(membership, request);
        return membershipRepository.save(membership);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        if (!membershipRepository.existsById(id)) {
            throw new IllegalArgumentException("Uplata nije pronađena.");
        }
        membershipRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Membership updateStatus(Long id, MembershipStatus status) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Uplata nije pronađena."));
        membership.setStatus(status);
        return membershipRepository.save(membership);
    }

    private void applyRequestData(Membership membership, MembershipRequest request) {
        validateRequest(request);

        membership.setVezbac(vezbacRepository.findById(request.getKorisnikId())
                .orElseThrow(() -> new IllegalArgumentException("Korisnik nije pronađen.")));

        membership.setClanarina(clanarinaRepository.findById(request.getClanarinaId())
                .orElseThrow(() -> new IllegalArgumentException("Clanarina nije pronađena.")));

        membership.setDatumUplate(request.getDatumUplate());
        membership.setDatumIsteka(request.getDatumIsteka());
        membership.setIznos(request.getIznos());
    }

    private void validateRequest(MembershipRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Telo zahteva je obavezno.");
        }

        if (request.getKorisnikId() == null) {
            throw new IllegalArgumentException("Korisnik je obavezan.");
        }

        if (request.getClanarinaId() == null) {
            throw new IllegalArgumentException("Clanarina je obavezna.");
        }

        if (request.getDatumUplate() == null) {
            throw new IllegalArgumentException("Datum uplate je obavezan.");
        }

        if (request.getDatumIsteka() == null) {
            throw new IllegalArgumentException("Datum isteka je obavezan.");
        }

        if (request.getIznos() == null) {
            throw new IllegalArgumentException("Iznos je obavezan.");
        }
    }
}
