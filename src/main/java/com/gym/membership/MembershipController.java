package com.gym.membership;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clanarine")
@RequiredArgsConstructor
public class MembershipController {

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

    @GetMapping("/vezbac/{id}")
    public ResponseEntity<List<MembershipResponse>> getByVezbac(@PathVariable Long id) {
        return ResponseEntity.ok(membershipService.getByVezbac(id).stream()
                .map(MembershipResponse::from)
                .toList());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<MembershipResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        MembershipStatus status = MembershipStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(MembershipResponse.from(membershipService.updateStatus(id, status)));
    }
}
