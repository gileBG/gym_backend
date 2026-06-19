package com.gym.membership;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramPaymentRepository extends JpaRepository<ProgramPayment, Long> {
    List<ProgramPayment> findByVezbacId(Long vezbacId);
    List<ProgramPayment> findByProgramId(Long programId);
}