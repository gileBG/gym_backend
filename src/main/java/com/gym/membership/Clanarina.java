package com.gym.membership;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "clanarine")
@Getter
@Setter
@NoArgsConstructor
public class Clanarina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String naziv;

    @Column(nullable = false)
    private BigDecimal cena;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip_clanarine", nullable = false)
    private TipClanarine tipClanarine;
}
