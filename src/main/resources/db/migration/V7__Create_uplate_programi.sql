DROP TABLE IF EXISTS uplate_programi;

CREATE TABLE uplate_programi (
    id BIGINT NOT NULL AUTO_INCREMENT,
    vezbac_id BIGINT NOT NULL,
    program_id BIGINT NOT NULL,
    datum_uplate DATE NOT NULL,
    datum_isteka DATE NOT NULL,
    iznos DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AKTIVNA',
    PRIMARY KEY (id),
    KEY idx_uplate_programi_vezbac_id (vezbac_id),
    KEY idx_uplate_programi_program_id (program_id),
    CONSTRAINT fk_uplate_programi_vezbac FOREIGN KEY (vezbac_id) REFERENCES vezbaci (id),
    CONSTRAINT fk_uplate_programi_program FOREIGN KEY (program_id) REFERENCES programi (id)
);