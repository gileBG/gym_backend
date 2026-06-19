package com.gym.membership;

import com.gym.program.Program;
import com.gym.user.Vezbac;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProgramPaymentResponse(
        Long id,
        VezbacSummary vezbac,
        ProgramSummary program,
        LocalDate datumUplate,
        LocalDate datumIsteka,
        BigDecimal iznos,
        MembershipStatus status
) {

    public static ProgramPaymentResponse from(ProgramPayment payment) {
        return new ProgramPaymentResponse(
                payment.getId(),
                VezbacSummary.from(payment.getVezbac()),
                ProgramSummary.from(payment.getProgram()),
                payment.getDatumUplate(),
                payment.getDatumIsteka(),
                payment.getIznos(),
                payment.getStatus()
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

    public record ProgramSummary(Long id, String naziv, BigDecimal cena, int trajanjeMeseci) {
        public static ProgramSummary from(Program program) {
            if (program == null) {
                return null;
            }

            return new ProgramSummary(
                    program.getId(),
                    program.getNaziv(),
                    program.getCena(),
                    program.getTrajanjeMeseci()
            );
        }
    }
}