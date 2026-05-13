package com.gym.user;

import com.gym.membership.TipClanarine;
import lombok.Data;

@Data
public class VezbacRequest {
    private String ime;
    private String prezime;
    private String adresa;
    private String email;
    private String lozinka;
    private TipClanarine tipClanarine;
}
