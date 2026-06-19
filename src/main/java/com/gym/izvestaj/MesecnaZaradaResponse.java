package com.gym.izvestaj;

import java.math.BigDecimal;
import java.util.List;

public record MesecnaZaradaResponse(
    int godina,
    int mesec,
    BigDecimal ukupnoClanarine,
    BigDecimal ukupnoProgrami,
    BigDecimal ukupnoSve,
    List<Stavka> stavke
) {
    public record Stavka(
        String tip,
        String naziv,
        BigDecimal iznos,
        int brojUplata
    ) {}
}
