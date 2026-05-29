package com.gym.program;

import com.gym.user.Role;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;
    private final UserRepository userRepository;

    public List<Program> getAll() {
        return programRepository.findAll();
    }

    public Program getById(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Program nije pronađen."));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'MENADZER')")
    public Program save(Program program) {
        if (program.getTrener() == null || program.getTrener().getId() == null) {
            throw new IllegalArgumentException("Trener je obavezan.");
        }

        User trener = userRepository.findById(program.getTrener().getId())
                .orElseThrow(() -> new IllegalArgumentException("Trener nije pronađen."));

        if (trener.getRola() != Role.TRENER) {
            throw new IllegalArgumentException("Izabrani zaposleni nije trener.");
        }

        program.setTrener(trener);
        return programRepository.save(program);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        programRepository.deleteById(id);
    }
}
