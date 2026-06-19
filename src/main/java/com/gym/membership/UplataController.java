package com.gym.membership;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/uplate")
@RequiredArgsConstructor
public class UplataController {

    private final MembershipService membershipService;

    @PostMapping
    public ResponseEntity<MembershipResponse> create(@RequestBody MembershipRequest request) {
        return ResponseEntity.ok(MembershipResponse.from(membershipService.create(request)));
    }

    @GetMapping
    public ResponseEntity<List<MembershipResponse>> getAll() {
        return ResponseEntity.ok(membershipService.getAll().stream()
                .map(MembershipResponse::from)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(MembershipResponse.from(membershipService.getById(id)));
    }

    @GetMapping("/vezbac/{id}")
    public ResponseEntity<List<MembershipResponse>> getByVezbac(@PathVariable Long id) {
        return ResponseEntity.ok(membershipService.getByVezbac(id).stream()
                .map(MembershipResponse::from)
                .toList());
    }

    @GetMapping("/korisnik/{id}")
    public ResponseEntity<List<MembershipResponse>> getByKorisnik(@PathVariable Long id) {
        return ResponseEntity.ok(membershipService.getByVezbac(id).stream()
                .map(MembershipResponse::from)
                .toList());
    }

    @GetMapping("/clanarina/{id}")
    public ResponseEntity<List<MembershipResponse>> getByClanarina(@PathVariable Long id) {
        return ResponseEntity.ok(membershipService.getByClanarina(id).stream()
                .map(MembershipResponse::from)
                .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipResponse> update(@PathVariable Long id, @RequestBody MembershipRequest request) {
        return ResponseEntity.ok(MembershipResponse.from(membershipService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        membershipService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
