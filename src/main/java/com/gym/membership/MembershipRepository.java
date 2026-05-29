package com.gym.membership;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByZaposleniId(Long zaposleniId);
    List<Membership> findByVezbacId(Long vezbacId);
}
