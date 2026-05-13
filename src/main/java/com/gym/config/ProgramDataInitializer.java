package com.gym.config;

import com.gym.program.Program;
import com.gym.program.ProgramRepository;
import com.gym.user.Role;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class ProgramDataInitializer {

    private final ProgramRepository programRepository;
    private final UserRepository userRepository;

    @Bean
    public ApplicationRunner seedPrograms() {
        return args -> {
            User trener = userRepository.findAllByRola(Role.TRENER).stream().findFirst().orElse(null);

            createIfMissing("MMA", "Borilacki trening visokog intenziteta", 3, new BigDecimal("8500"), trener);
            createIfMissing("Pilates", "Kontrola pokreta, snaga i fleksibilnost", 3, new BigDecimal("6500"), trener);
            createIfMissing("Crossfit", "Funkcionalni trening i kondicija", 3, new BigDecimal("9000"), trener);
            createIfMissing("Military", "Snaga, izdrzljivost i disciplina", 3, new BigDecimal("9500"), trener);
            createIfMissing("Personalni Treninzi", "Individualni rad sa trenerom", 1, new BigDecimal("15000"), trener);
        };
    }

    private void createIfMissing(String naziv, String opis, int trajanjeMeseci, BigDecimal cena, User trener) {
        if (programRepository.findByNazivIgnoreCase(naziv).isPresent()) {
            return;
        }

        Program program = new Program();
        program.setNaziv(naziv);
        program.setOpis(opis);
        program.setTrajanjeMeseci(trajanjeMeseci);
        program.setCena(cena);
        program.setTrener(trener);
        programRepository.save(program);
    }
}
