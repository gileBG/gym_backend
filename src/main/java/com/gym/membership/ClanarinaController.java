package com.gym.membership;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clanarine-cenovnik")
@RequiredArgsConstructor
public class ClanarinaController {

    private final ClanarinaService clanarinaService;

    @PostMapping
    public ResponseEntity<Clanarina> create(@RequestBody ClanarinaRequest request) {
        return ResponseEntity.ok(clanarinaService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<Clanarina>> getAll() {
        return ResponseEntity.ok(clanarinaService.getAll());
    }
}
