package com.gym.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vezbaci")
@RequiredArgsConstructor
public class VezbacController {

    private final VezbacService vezbacService;

    @GetMapping
    public ResponseEntity<List<Vezbac>> getAll() {
        return ResponseEntity.ok(vezbacService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vezbac> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vezbacService.getById(id));
    }

    @GetMapping("/zaposleni/{zaposleniId}")
    public ResponseEntity<Vezbac> getByZaposleniId(@PathVariable Long zaposleniId) {
        return ResponseEntity.ok(vezbacService.getByZaposleniId(zaposleniId));
    }

    @PostMapping
    public ResponseEntity<Vezbac> create(@RequestBody VezbacRequest request) {
        return ResponseEntity.ok(vezbacService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vezbac> update(@PathVariable Long id, @RequestBody VezbacRequest request) {
        return ResponseEntity.ok(vezbacService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vezbacService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
