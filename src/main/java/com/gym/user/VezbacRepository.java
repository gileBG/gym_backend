package com.gym.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VezbacRepository extends JpaRepository<Vezbac, Long> {
    Optional<Vezbac> findByEmail(String email);
}
