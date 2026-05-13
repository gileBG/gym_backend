INSERT INTO clanarine (naziv, cena, tip_clanarine)
SELECT 'Dnevna karta', 700.00, 'STANDARD'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM clanarine WHERE naziv = 'Dnevna karta');

INSERT INTO clanarine (naziv, cena, tip_clanarine)
SELECT 'Dnevna karta 7 dana', 3000.00, 'STANDARD'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM clanarine WHERE naziv = 'Dnevna karta 7 dana');

INSERT INTO clanarine (naziv, cena, tip_clanarine)
SELECT 'Dnevna karta 12 dana', 3590.00, 'STANDARD'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM clanarine WHERE naziv = 'Dnevna karta 12 dana');

INSERT INTO clanarine (naziv, cena, tip_clanarine)
SELECT 'Mesecna clanarina', 3990.00, 'STANDARD'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM clanarine WHERE naziv = 'Mesecna clanarina');

INSERT INTO clanarine (naziv, cena, tip_clanarine)
SELECT '3 meseca clanarina', 10790.00, 'STANDARD'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM clanarine WHERE naziv = '3 meseca clanarina');

INSERT INTO clanarine (naziv, cena, tip_clanarine)
SELECT '6 meseci clanarina', 20190.00, 'STANDARD'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM clanarine WHERE naziv = '6 meseci clanarina');

INSERT INTO clanarine (naziv, cena, tip_clanarine)
SELECT '12 meseci clanarina', 37990.00, 'STANDARD'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM clanarine WHERE naziv = '12 meseci clanarina');

INSERT INTO clanarine (naziv, cena, tip_clanarine)
SELECT 'Clanarina za studente, pripadnike vojske i policije', 2990.00, 'POLICIJA_VOJSKA'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM clanarine WHERE naziv = 'Clanarina za studente, pripadnike vojske i policije');

INSERT INTO clanarine (naziv, cena, tip_clanarine)
SELECT 'Porodicni paket (minimum 3 clana porodice)', 2990.00, 'STANDARD'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM clanarine WHERE naziv = 'Porodicni paket (minimum 3 clana porodice)');

INSERT INTO clanarine (naziv, cena, tip_clanarine)
SELECT 'SAMS - Studentska asocijacija za medjunarodnu saradnju', 2990.00, 'STUDENT'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM clanarine WHERE naziv = 'SAMS - Studentska asocijacija za medjunarodnu saradnju');

SELECT id, naziv, cena, tip_clanarine FROM clanarine ORDER BY id;
