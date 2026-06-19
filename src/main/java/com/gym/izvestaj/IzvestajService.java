package com.gym.izvestaj;

import com.gym.membership.Membership;
import com.gym.membership.MembershipRepository;
import com.gym.membership.ProgramPayment;
import com.gym.membership.ProgramPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IzvestajService {

    private final MembershipRepository membershipRepository;
    private final ProgramPaymentRepository programPaymentRepository;

    public MesecnaZaradaResponse getZaradaPoMesecu(int godina, int mesec) {
        LocalDate pocetak = LocalDate.of(godina, mesec, 1);
        LocalDate kraj = pocetak.withDayOfMonth(pocetak.lengthOfMonth());

        List<Membership> clanarine = membershipRepository.findAll().stream()
                .filter(m -> !m.getDatumUplate().isBefore(pocetak) && !m.getDatumUplate().isAfter(kraj))
                .toList();

        List<ProgramPayment> programi = programPaymentRepository.findAll().stream()
                .filter(p -> !p.getDatumUplate().isBefore(pocetak) && !p.getDatumUplate().isAfter(kraj))
                .toList();

        List<MesecnaZaradaResponse.Stavka> stavke = new ArrayList<>();

        // Grupisi clanarine po nazivu
        Map<String, List<Membership>> clanarinePoNazivu = clanarine.stream()
                .collect(Collectors.groupingBy(m -> m.getClanarina().getNaziv()));

        for (Map.Entry<String, List<Membership>> entry : clanarinePoNazivu.entrySet()) {
            BigDecimal sum = entry.getValue().stream()
                    .map(Membership::getIznos)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stavke.add(new MesecnaZaradaResponse.Stavka(
                    "Članarina", entry.getKey(), sum, entry.getValue().size()));
        }

        // Grupisi programe po nazivu
        Map<String, List<ProgramPayment>> programiPoNazivu = programi.stream()
                .collect(Collectors.groupingBy(p -> p.getProgram().getNaziv()));

        for (Map.Entry<String, List<ProgramPayment>> entry : programiPoNazivu.entrySet()) {
            BigDecimal sum = entry.getValue().stream()
                    .map(ProgramPayment::getIznos)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stavke.add(new MesecnaZaradaResponse.Stavka(
                    "Program", entry.getKey(), sum, entry.getValue().size()));
        }

        BigDecimal ukupnoClanarine = clanarine.stream()
                .map(Membership::getIznos)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ukupnoProgrami = programi.stream()
                .map(ProgramPayment::getIznos)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MesecnaZaradaResponse(
                godina, mesec,
                ukupnoClanarine, ukupnoProgrami,
                ukupnoClanarine.add(ukupnoProgrami),
                stavke);
    }
}
