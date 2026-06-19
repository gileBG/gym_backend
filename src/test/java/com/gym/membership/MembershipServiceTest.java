package com.gym.membership;

import com.gym.user.VezbacRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private VezbacRepository vezbacRepository;

    @Mock
    private ClanarinaRepository clanarinaRepository;

    @InjectMocks
    private MembershipService membershipService;

    private MembershipRequest validRequest() {
        MembershipRequest request = new MembershipRequest();
        request.setKorisnikId(20L);
        request.setClanarinaId(10L);
        request.setDatumUplate(LocalDate.of(2026, 5, 1));
        request.setDatumIsteka(LocalDate.of(2026, 6, 1));
        request.setIznos(BigDecimal.valueOf(3500));
        return request;
    }

    private Membership existingMembership() {
        Membership membership = new Membership();
        membership.setId(1L);
        membership.setDatumUplate(LocalDate.of(2026, 4, 1));
        membership.setDatumIsteka(LocalDate.of(2026, 5, 1));
        membership.setIznos(BigDecimal.valueOf(3000));
        return membership;
    }

    private Clanarina existingClanarina() {
        Clanarina clanarina = new Clanarina();
        clanarina.setId(10L);
        clanarina.setNaziv("Mesecna clanarina");
        clanarina.setCena(BigDecimal.valueOf(3500));
        clanarina.setTipClanarine(TipClanarine.STANDARD);
        return clanarina;
    }

    @Test
    void getById_postojeca_vracaUplatu() {
        Membership membership = existingMembership();
        when(membershipRepository.findById(1L)).thenReturn(Optional.of(membership));

        Membership result = membershipService.getById(1L);

        assertThat(result).isSameAs(membership);
    }

    @Test
    void getById_nepostojeca_bacaIzuzetak() {
        when(membershipRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> membershipService.getById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Uplata nije pronađena.");
    }

    @Test
    void update_postojeca_azuriraPolja() {
        Membership existing = existingMembership();
        MembershipRequest request = validRequest();

        when(membershipRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(vezbacRepository.findById(20L)).thenReturn(Optional.of(new com.gym.user.Vezbac()));
        when(clanarinaRepository.findById(10L)).thenReturn(Optional.of(existingClanarina()));
        when(membershipRepository.save(existing)).thenReturn(existing);

        Membership result = membershipService.update(1L, request);

        assertThat(result.getDatumUplate()).isEqualTo(LocalDate.of(2026, 5, 1));
        assertThat(result.getDatumIsteka()).isEqualTo(LocalDate.of(2026, 6, 1));
        assertThat(result.getIznos()).isEqualByComparingTo(BigDecimal.valueOf(3500));
        assertThat(result.getClanarina()).isNotNull();
    }

    @Test
    void create_saClanarinom_upisujeUplatuClanarine() {
        MembershipRequest request = validRequest();
        Membership saved = existingMembership();

        when(vezbacRepository.findById(20L)).thenReturn(Optional.of(new com.gym.user.Vezbac()));
        when(clanarinaRepository.findById(10L)).thenReturn(Optional.of(existingClanarina()));
        when(membershipRepository.save(org.mockito.ArgumentMatchers.any(Membership.class))).thenReturn(saved);

        Membership result = membershipService.create(request);

        assertThat(result).isSameAs(saved);
        verify(membershipRepository).save(org.mockito.ArgumentMatchers.any(Membership.class));
    }

    @Test
    void update_nepostojeca_bacaIzuzetak() {
        when(membershipRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> membershipService.update(1L, validRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Uplata nije pronađena.");
    }

    @Test
    void delete_postojeca_brise() {
        when(membershipRepository.existsById(1L)).thenReturn(true);

        membershipService.delete(1L);

        verify(membershipRepository).deleteById(1L);
    }

    @Test
    void delete_nepostojeca_bacaIzuzetak() {
        when(membershipRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> membershipService.delete(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Uplata nije pronađena.");
    }
}
