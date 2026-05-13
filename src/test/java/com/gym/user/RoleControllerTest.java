package com.gym.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoleControllerTest {

    private final RoleController roleController = new RoleController();

    @Test
    void getAll_vracaSveRole() {
        ResponseEntity<List<String>> response = roleController.getAll();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).containsExactly(
                "ADMIN",
                "TRENER",
                "FRONT_DESK",
                "CISTACICA",
                "MENADZER"
        );
    }
}