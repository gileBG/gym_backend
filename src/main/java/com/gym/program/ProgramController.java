package com.gym.program;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programi")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;

    @GetMapping
    public ResponseEntity<List<Program>> getAll() {
        return ResponseEntity.ok(programService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Program> getById(@PathVariable Long id) {
        return ResponseEntity.ok(programService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Program> create(@RequestBody Program program) {
        return ResponseEntity.ok(programService.save(program));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Program> update(@PathVariable Long id, @RequestBody Program program) {
        program.setId(id);
        return ResponseEntity.ok(programService.save(program));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        programService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
