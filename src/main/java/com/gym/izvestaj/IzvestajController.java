package com.gym.izvestaj;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/izvestaji")
@RequiredArgsConstructor
public class IzvestajController {

    private final IzvestajService izvestajService;

    @GetMapping("/zarada-po-mesecu")
    public ResponseEntity<MesecnaZaradaResponse> getZaradaPoMesecu(
            @RequestParam int godina,
            @RequestParam int mesec) {
        return ResponseEntity.ok(izvestajService.getZaradaPoMesecu(godina, mesec));
    }
}
