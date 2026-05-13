package com.gym.membership;

import com.gym.program.ProgramRepository;
import com.gym.user.UserRepository;
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
    private final UserRepository userRepository;
    private final VezbacRepository vezbacRepository;
    private final ProgramRepository programRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER')")
    public Membership create(MembershipRequest request) {
        Membership membership = new Membership();
        Long zaposleniId = request.getZaposleniId();
        membership.setZaposleni(userRepository.findById(zaposleniId)
                .orElseThrow(() -> new IllegalArgumentException("Zaposleni nije pronađen.")));
        vezbacRepository.findById(zaposleniId).ifPresent(membership::setVezbac);
        membership.setProgram(programRepository.findById(request.getProgramId())
                .orElseThrow(() -> new IllegalArgumentException("Program nije pronađen.")));
        membership.setDatumUplate(request.getDatumUplate());
        membership.setDatumIsteka(request.getDatumIsteka());
        membership.setIznos(request.getIznos());
        return membershipRepository.save(membership);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER')")
    public List<Membership> getAll() {
        return membershipRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Membership> getByZaposleni(Long zaposleniId) {
        return membershipRepository.findByZaposleniId(zaposleniId);
    }
}
