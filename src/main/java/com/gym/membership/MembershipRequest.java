package com.gym.membership;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MembershipRequest {
    private Long zaposleniId;
    private Long programId;
    private LocalDate datumUplate;
    private LocalDate datumIsteka;
    private BigDecimal iznos;
}
