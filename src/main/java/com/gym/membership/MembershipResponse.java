package com.gym.membership;

import com.gym.user.Vezbac;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MembershipResponse(
        Long id,
        VezbacSummary vezbac,
        ClanarinaSummary clanarina,
        LocalDate datumUplate,
        LocalDate datumIsteka,
        BigDecimal iznos,
        MembershipStatus status
) {

    public static MembershipResponse from(Membership membership) {
        return new MembershipResponse(
                membership.getId(),
                VezbacSummary.from(membership.getVezbac()),
                ClanarinaSummary.from(membership.getClanarina()),
                membership.getDatumUplate(),
                membership.getDatumIsteka(),
                membership.getIznos(),
                membership.getStatus()
        );
    }

    public record VezbacSummary(Long id, String ime, String prezime, String email) {
        public static VezbacSummary from(Vezbac vezbac) {
            if (vezbac == null) {
                return null;
            }

            return new VezbacSummary(
                    vezbac.getId(),
                    vezbac.getIme(),
                    vezbac.getPrezime(),
                    vezbac.getEmail()
            );
        }
    }

    public record ClanarinaSummary(Long id, String naziv, BigDecimal cena, TipClanarine tipClanarine) {
        public static ClanarinaSummary from(Clanarina clanarina) {
            if (clanarina == null) {
                return null;
            }

            return new ClanarinaSummary(
                    clanarina.getId(),
                    clanarina.getNaziv(),
                    clanarina.getCena(),
                    clanarina.getTipClanarine()
            );
        }
    }
}