package com.gym.membership;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClanarinaRequest {
    private String naziv;
    private BigDecimal cena;
    private TipClanarine tipClanarine;
}
