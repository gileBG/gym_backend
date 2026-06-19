DROP TABLE IF EXISTS uplate;

DROP TABLE IF EXISTS uplate_clanarina;

CREATE TABLE uplate_clanarina (
    id BIGINT NOT NULL AUTO_INCREMENT,
    vezbac_id BIGINT NOT NULL,
    clanarina_id BIGINT NOT NULL,
    datum_uplate DATE NOT NULL,
    datum_isteka DATE NOT NULL,
    iznos DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AKTIVNA',
    PRIMARY KEY (id),
    KEY idx_uplate_clanarina_vezbac_id (vezbac_id),
    KEY idx_uplate_clanarina_clanarina_id (clanarina_id),
    CONSTRAINT fk_uplate_clanarina_vezbac FOREIGN KEY (vezbac_id) REFERENCES vezbaci (id),
    CONSTRAINT fk_uplate_clanarina_clanarina FOREIGN KEY (clanarina_id) REFERENCES clanarine (id)
);