-- =====================================================================
-- RepObra / Clarifica Obra API
-- Correção: general_information passa a ser por obra
-- Data: 2026-10-29
--
-- O que este script faz:
--   1. Move general_information de phase_id para construction_id
--   2. Alinha o check de topography com os valores usados pela UI
--   3. Cria uma linha padrão para cada obra existente
--   4. Registra a migração
--
-- Execução:
--   psql -h localhost -p 5432 -U postgres -d construction -1 -v ON_ERROR_STOP=1 -f este_arquivo.sql
-- =====================================================================

\set ON_ERROR_STOP on
SET client_encoding = 'UTF8';

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM migration.schema_migration
        WHERE version = '2026-10-29-fix-general-information'
    ) THEN
        RAISE EXCEPTION 'Migração 2026-10-29-fix-general-information já aplicada';
    END IF;

    -- A tabela estava vazia na inspeção. Se houver dados, não podemos
    -- converter phase_id em construction_id sem uma regra de negócio.
    IF EXISTS (SELECT 1 FROM general_information) THEN
        RAISE EXCEPTION 'general_information não está vazia; ajuste os dados antes de executar esta migração';
    END IF;
END $$;

-- ---------------------------------------------------------------------
-- 1. Remover constraints antigas
-- ---------------------------------------------------------------------
DO $$
DECLARE
    cname text;
BEGIN
    -- FK para phases
    SELECT conname INTO cname
    FROM pg_constraint
    WHERE conrelid = 'general_information'::regclass
      AND contype = 'f'
      AND confrelid = 'phases'::regclass
    LIMIT 1;

    IF cname IS NOT NULL THEN
        EXECUTE format('ALTER TABLE general_information DROP CONSTRAINT %I', cname);
    END IF;

    -- UNIQUE de phase_id
    SELECT conname INTO cname
    FROM pg_constraint
    WHERE conrelid = 'general_information'::regclass
      AND contype = 'u'
      AND conkey = ARRAY[
          (SELECT attnum
           FROM pg_attribute
           WHERE attrelid = 'general_information'::regclass
             AND attname = 'phase_id')
      ]
    LIMIT 1;

    IF cname IS NOT NULL THEN
        EXECUTE format('ALTER TABLE general_information DROP CONSTRAINT %I', cname);
    END IF;

    -- CHECK de topography (valores em inglês)
    SELECT conname INTO cname
    FROM pg_constraint
    WHERE conrelid = 'general_information'::regclass
      AND contype = 'c'
      AND pg_get_constraintdef(oid) ILIKE '%topography%'
    LIMIT 1;

    IF cname IS NOT NULL THEN
        EXECUTE format('ALTER TABLE general_information DROP CONSTRAINT %I', cname);
    END IF;
END $$;

-- ---------------------------------------------------------------------
-- 2. Renomear phase_id -> construction_id
-- ---------------------------------------------------------------------
ALTER TABLE general_information
    RENAME COLUMN phase_id TO construction_id;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conrelid = 'general_information'::regclass
          AND conname = 'general_information_phase_id_not_null'
    ) THEN
        ALTER TABLE general_information
            RENAME CONSTRAINT general_information_phase_id_not_null
            TO general_information_construction_id_not_null;
    END IF;
END $$;

-- ---------------------------------------------------------------------
-- 3. Normalizar valores antigos de topography, se houver
-- ---------------------------------------------------------------------
UPDATE general_information
SET topography = CASE upper(topography)
    WHEN 'FLAT'        THEN 'plana'
    WHEN 'SLOPED'      THEN 'inclinada'
    WHEN 'HILLY'       THEN 'acidentada'
    WHEN 'MOUNTAINOUS' THEN 'com desnível'
    ELSE topography
END
WHERE upper(topography) IN ('FLAT', 'SLOPED', 'HILLY', 'MOUNTAINOUS');

-- ---------------------------------------------------------------------
-- 4. Novas constraints
-- ---------------------------------------------------------------------
ALTER TABLE general_information
    ADD CONSTRAINT fk_general_information_construction
    FOREIGN KEY (construction_id) REFERENCES constructions(id) ON DELETE CASCADE;

ALTER TABLE general_information
    ADD CONSTRAINT general_information_construction_id_unique
    UNIQUE (construction_id);

ALTER TABLE general_information
    ADD CONSTRAINT general_information_topography_check
    CHECK (topography IN ('plana', 'inclinada', 'acidentada', 'com desnível'));

-- ---------------------------------------------------------------------
-- 5. Linha padrão para cada obra existente
-- ---------------------------------------------------------------------
INSERT INTO general_information
    (id, construction_id, address, land_area, topography, start_date)
SELECT
    gen_random_uuid()::text,
    c.id,
    'Não informado',
    0,
    'plana',
    CURRENT_DATE
FROM constructions c
WHERE NOT EXISTS (
    SELECT 1
    FROM general_information gi
    WHERE gi.construction_id = c.id
);

-- ---------------------------------------------------------------------
-- 6. Registrar a migração
-- ---------------------------------------------------------------------
INSERT INTO migration.schema_migration (version)
VALUES ('2026-10-29-fix-general-information');
