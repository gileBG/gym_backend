package com.gym.program;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gym.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "programi")
@Getter
@Setter
@NoArgsConstructor
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String naziv;

    private String opis;

    @Column(nullable = false)
    private BigDecimal cena;

    @Column(nullable = false)
    private int trajanjeMeseci;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trener_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User trener;
}
