package com.gym.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gym.membership.TipClanarine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vezbaci")
@Getter
@Setter
@NoArgsConstructor
public class Vezbac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ime;

    @Column(nullable = false)
    private String prezime;

    private String adresa;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String lozinka;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipClanarine tipClanarine = TipClanarine.STANDARD;

    @Column(name = "avatar_url", length = 512)
    private String avatarUrl;
}
