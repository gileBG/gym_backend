package com.gym.membership;

import com.gym.program.Program;
import com.gym.program.ProgramRepository;
import com.gym.user.Vezbac;
import com.gym.user.VezbacRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramPaymentService {

    private final ProgramPaymentRepository programPaymentRepository;
    private final VezbacRepository vezbacRepository;
    private final ProgramRepository programRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'ZAPOSLENI', 'VEZBAC')")
    public List<ProgramPaymentResponse> getAll() {
        return programPaymentRepository.findAll().stream()
                .map(ProgramPaymentResponse::from)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ZAPOSLENI', 'VEZBAC')")
    public ProgramPaymentResponse getById(Long id) {
        ProgramPayment payment = programPaymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Uplata programa nije pronađena."));
        return ProgramPaymentResponse.from(payment);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ZAPOSLENI', 'VEZBAC')")
    public List<ProgramPaymentResponse> getByVezbacId(Long vezbacId) {
        return programPaymentRepository.findByVezbacId(vezbacId).stream()
                .map(ProgramPaymentResponse::from)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ZAPOSLENI')")
    public List<ProgramPaymentResponse> getByProgramId(Long programId) {
        return programPaymentRepository.findByProgramId(programId).stream()
                .map(ProgramPaymentResponse::from)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProgramPaymentResponse create(ProgramPaymentRequest request) {
        ProgramPayment saved = programPaymentRepository.save(toEntity(request, new ProgramPayment()));
        return ProgramPaymentResponse.from(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProgramPaymentResponse update(Long id, ProgramPaymentRequest request) {
        ProgramPayment existing = programPaymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Uplata programa nije pronađena."));

        ProgramPayment saved = programPaymentRepository.save(toEntity(request, existing));
        return ProgramPaymentResponse.from(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        if (!programPaymentRepository.existsById(id)) {
            throw new IllegalArgumentException("Uplata programa nije pronađena.");
        }
        programPaymentRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProgramPaymentResponse updateStatus(Long id, MembershipStatus status) {
        ProgramPayment payment = programPaymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Uplata programa nije pronađena."));
        payment.setStatus(status);
        ProgramPayment saved = programPaymentRepository.save(payment);
        return ProgramPaymentResponse.from(saved);
    }

    private ProgramPayment toEntity(ProgramPaymentRequest request, ProgramPayment entity) {
        if (request.getKorisnikId() == null) {
            throw new IllegalArgumentException("Korisnik je obavezan.");
        }
        if (request.getProgramId() == null) {
            throw new IllegalArgumentException("Program je obavezan.");
        }
        if (request.getDatumUplate() == null || request.getDatumIsteka() == null) {
            throw new IllegalArgumentException("Datum uplate i datum isteka su obavezni.");
        }
        if (request.getIznos() == null) {
            throw new IllegalArgumentException("Iznos je obavezan.");
        }

        Vezbac vezbac = vezbacRepository.findById(request.getKorisnikId())
                .orElseThrow(() -> new IllegalArgumentException("Korisnik nije pronađen."));
        Program program = programRepository.findById(request.getProgramId())
                .orElseThrow(() -> new IllegalArgumentException("Program nije pronađen."));

        entity.setVezbac(vezbac);
        entity.setProgram(program);
        entity.setDatumUplate(request.getDatumUplate());
        entity.setDatumIsteka(request.getDatumIsteka());
        entity.setIznos(request.getIznos());
        entity.setStatus(request.getDatumIsteka().isBefore(java.time.LocalDate.now()) ? MembershipStatus.ISTEKLA : MembershipStatus.AKTIVNA);

        return entity;
    }
}