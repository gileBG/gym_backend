package com.gym.membership;

import com.gym.program.Program;
import com.gym.user.User;
import com.gym.user.Vezbac;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "uplate")
@Getter
@Setter
@NoArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zaposleni_id", nullable = false)
    private User zaposleni;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vezbac_id")
    private Vezbac vezbac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Column(nullable = false)
    private LocalDate datumUplate;

    @Column(nullable = false)
    private LocalDate datumIsteka;

    @Column(nullable = false)
    private BigDecimal iznos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status = MembershipStatus.AKTIVNA;
}
