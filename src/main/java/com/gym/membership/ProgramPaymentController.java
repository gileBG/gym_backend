package com.gym.membership;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/uplate-programi")
@RequiredArgsConstructor
public class ProgramPaymentController {

    private final ProgramPaymentService programPaymentService;

    @GetMapping
    public ResponseEntity<List<ProgramPaymentResponse>> getAll() {
        return ResponseEntity.ok(programPaymentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramPaymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(programPaymentService.getById(id));
    }

    @GetMapping("/vezbac/{id}")
    public ResponseEntity<List<ProgramPaymentResponse>> getByVezbacId(@PathVariable Long id) {
        return ResponseEntity.ok(programPaymentService.getByVezbacId(id));
    }

    @GetMapping("/korisnik/{id}")
    public ResponseEntity<List<ProgramPaymentResponse>> getByKorisnikId(@PathVariable Long id) {
        return ResponseEntity.ok(programPaymentService.getByVezbacId(id));
    }

    @GetMapping("/program/{id}")
    public ResponseEntity<List<ProgramPaymentResponse>> getByProgramId(@PathVariable Long id) {
        return ResponseEntity.ok(programPaymentService.getByProgramId(id));
    }

    @PostMapping
    public ResponseEntity<ProgramPaymentResponse> create(@RequestBody ProgramPaymentRequest request) {
        return ResponseEntity.ok(programPaymentService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramPaymentResponse> update(@PathVariable Long id, @RequestBody ProgramPaymentRequest request) {
        return ResponseEntity.ok(programPaymentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        programPaymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProgramPaymentResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        MembershipStatus status = MembershipStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(programPaymentService.updateStatus(id, status));
    }
}