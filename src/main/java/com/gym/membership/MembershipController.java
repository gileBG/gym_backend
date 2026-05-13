package com.gym.membership;

import com.gym.program.Program;
import com.gym.program.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clanarine")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;
    private final ProgramService programService;

    @PostMapping
    public ResponseEntity<Membership> create(@RequestBody MembershipRequest request) {
        return ResponseEntity.ok(membershipService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<Program>> getAll() {
        return ResponseEntity.ok(programService.getAll());
    }

    @GetMapping("/zaposleni/{id}")
    public ResponseEntity<List<Membership>> getByZaposleni(@PathVariable Long id) {
        return ResponseEntity.ok(membershipService.getByZaposleni(id));
    }
}
