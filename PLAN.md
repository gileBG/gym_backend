# Plan aplikacije — Gym Management System

## Opis

REST API za upravljanje teretanom: praćenje uplata članarine, odabir programa, pristup profilu vežbača.

## Role

| Rola | Opis |
|------|------|
| `ADMIN` | Pun pristup — upravljanje zaposlenima, programima, uplatama |
| `TRENER` | Rad sa treninzima i pregled vežbača |
| `FRONT_DESK` | Evidencija članarina i operativni unos |
| `CISTACICA` | Ograničen pristup pregledima |
| `MENADZER` | Operativni nadzor i upravljanje sadržajem |
| `VEZBAC` | Pristup sopstvenom profilu, programima i istoriji uplata |

## Entiteti (baza podataka — MySQL)

- **Zaposleni** — id, ime, prezime, email, lozinka (hash), rola, datum_registracije
- **Vezbac** — id, zaposleni_id, ime, prezime, adresa, email, lozinka, tip_clanarine
- **Program** — id, naziv, opis, cena, trajanje_meseci
- **Clanarina** — id, zaposleni_id, program_id, datum_uplate, datum_isteka, iznos, status
- **Profil** — id, zaposleni_id, telefon, adresa, datum_rodjenja

## Funkcionalnosti

### Auth
- Registracija vežbača
- Login (vraća JWT token)

### Admin / Zaposleni
- CRUD zaposlenih
- CRUD programa
- Unos i pregled uplata članarine
- Pregled svih vežbača i njihovih programa

### Vežbač
- Pregled sopstvenog profila
- Pregled aktivnih programa i istorije uplata
- Izmena sopstvenih podataka

## Arhitektura
