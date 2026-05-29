package com.gym.membership;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClanarinaServiceTest {

    @Mock
    private ClanarinaRepository clanarinaRepository;

    @InjectMocks
    private ClanarinaService clanarinaService;

    private ClanarinaRequest validRequest() {
        ClanarinaRequest request = new ClanarinaRequest();
        request.setNaziv("Mesecna");
        request.setCena(BigDecimal.valueOf(3500));
        request.setTipClanarine(TipClanarine.STANDARD);
        return request;
    }

    private Clanarina existingClanarina() {
        Clanarina clanarina = new Clanarina();
        clanarina.setId(1L);
        clanarina.setNaziv("Stara");
        clanarina.setCena(BigDecimal.valueOf(3000));
        clanarina.setTipClanarine(TipClanarine.STUDENT);
        return clanarina;
    }

    @Test
    void getAll_vracaListu() {
        when(clanarinaRepository.findAll()).thenReturn(List.of(existingClanarina()));

        assertThat(clanarinaService.getAll()).hasSize(1);
    }

    @Test
    void create_cuvaINazivTrimuje() {
        ClanarinaRequest request = validRequest();
        request.setNaziv("  Mesecna  ");

        when(clanarinaRepository.save(any(Clanarina.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Clanarina result = clanarinaService.create(request);

        assertThat(result.getNaziv()).isEqualTo("Mesecna");
        assertThat(result.getCena()).isEqualByComparingTo(BigDecimal.valueOf(3500));
        assertThat(result.getTipClanarine()).isEqualTo(TipClanarine.STANDARD);
    }

    @Test
    void update_postojeca_azuriraPolja() {
        ClanarinaRequest request = validRequest();
        Clanarina existing = existingClanarina();

        when(clanarinaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clanarinaRepository.save(existing)).thenReturn(existing);

        Clanarina result = clanarinaService.update(1L, request);

        assertThat(result.getNaziv()).isEqualTo("Mesecna");
        assertThat(result.getCena()).isEqualByComparingTo(BigDecimal.valueOf(3500));
        assertThat(result.getTipClanarine()).isEqualTo(TipClanarine.STANDARD);
    }

    @Test
    void update_nepostojeca_bacaIzuzetak() {
        when(clanarinaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clanarinaService.update(1L, validRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Clanarina nije pronađena.");
    }

    @Test
    void delete_postojeca_brise() {
        when(clanarinaRepository.existsById(1L)).thenReturn(true);

        clanarinaService.delete(1L);

        verify(clanarinaRepository).deleteById(1L);
    }

    @Test
    void delete_nepostojeca_bacaIzuzetak() {
        when(clanarinaRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> clanarinaService.delete(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Clanarina nije pronađena.");
    }
}
