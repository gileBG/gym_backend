package com.gym.program;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgramServiceTest {

    @Mock
    private ProgramRepository programRepository;

    @InjectMocks
    private ProgramService programService;

    private Program testProgram() {
        Program p = new Program();
        p.setId(1L);
        p.setNaziv("Fitness");
        p.setCena(BigDecimal.valueOf(3000));
        p.setTrajanjeMeseci(1);
        return p;
    }

    @Test
    void getAll_vraceListu() {
        when(programRepository.findAll()).thenReturn(List.of(testProgram()));
        assertThat(programService.getAll()).hasSize(1);
    }

    @Test
    void getById_pronasjen_vraceProgram() {
        when(programRepository.findById(1L)).thenReturn(Optional.of(testProgram()));
        assertThat(programService.getById(1L).getNaziv()).isEqualTo("Fitness");
    }

    @Test
    void getById_nijePronadjen_bacaIzuzetak() {
        when(programRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> programService.getById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void save_sacuvaProgramiVraceGa() {
        Program p = testProgram();
        when(programRepository.save(any(Program.class))).thenReturn(p);
        assertThat(programService.save(p).getNaziv()).isEqualTo("Fitness");
    }

    @Test
    void delete_pozivaBrisanje() {
        programService.delete(1L);
        verify(programRepository).deleteById(1L);
    }
}
