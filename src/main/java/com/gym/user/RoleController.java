package com.gym.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @GetMapping
    public ResponseEntity<List<String>> getAll() {
        List<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .toList();
        return ResponseEntity.ok(roles);
    }
}