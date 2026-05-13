package com.gym.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private UserRequest validRequest;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        validRequest = new UserRequest();
        validRequest.setIme("Jovan");
        validRequest.setPrezime("Jovanović");
        validRequest.setEmail("jovan@test.com");
        validRequest.setLozinka("Lozinka123");
        validRequest.setRola(Role.TRENER);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_uspesno_dodajZaposlenogoSaValidnimPodacima() throws Exception {
        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/zaposleni")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ime").value("Jovan"))
                .andExpect(jsonPath("$.prezime").value("Jovanović"))
                .andExpect(jsonPath("$.email").value("jovan@test.com"))
                .andExpect(jsonPath("$.rola").value("TRENER"))
                .andReturn();

        // Verify u bazi
        long count = userRepository.count();
        assertThat(count).isEqualTo(1);
        
        User savedUser = userRepository.findByEmail("jovan@test.com").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getIme()).isEqualTo("Jovan");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_bacaIzuzetak_kadaEmailVecPostoji() throws Exception {
        // Arrange - Prvo dodaj zaposlenog preko API-ja
        UserRequest firstRequest = new UserRequest();
        firstRequest.setIme("Marko");
        firstRequest.setPrezime("Marković");
        firstRequest.setEmail("jovan@test.com");
        firstRequest.setLozinka("Lozinka123");
        firstRequest.setRola(Role.TRENER);
        
        mockMvc.perform(post("/api/zaposleni")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isOk());

        // Act & Assert - Pokušaj sa istim emailom, trebalo bi da dobije istu email adresu
        validRequest.setEmail("jovan@test.com");
        mockMvc.perform(post("/api/zaposleni")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_bacaIzuzetak_kadaRolaNijeProsledjena() throws Exception {
        // Arrange
        validRequest.setRola(null);

        // Act & Assert
        mockMvc.perform(post("/api/zaposleni")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "TRENER")
    void create_zabranjen_kakoKorisnikNijeAdmin() throws Exception {
        // Act & Assert - Samo ADMIN može da dodede zaposlenog
        mockMvc.perform(post("/api/zaposleni")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void create_zahtevAutentifikaciju() throws Exception {
        // Act & Assert - Bez JWT tokena vraća 403 Forbidden (pristup odbijen)
        mockMvc.perform(post("/api/zaposleni")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isForbidden());
    }
}
