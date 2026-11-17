-- =====================================================================
-- RepObra / Clarifica Obra API
-- Migração: modelo antigo (fases por usuário) -> modelo novo (phases)
-- Data: 2026-10-29
--
-- O que este script faz:
--   1. Cria um backup lógico de todas as tabelas públicas
--   2. Corrige a integridade do modelo novo (PKs, FKs, NOT NULL, índices, CHECKs)
--   3. Migra os dados das tabelas antigas para o modelo novo
--   4. Move as tabelas antigas para o schema "legacy"
--   5. Registra a migração em migration.schema_migration
--
-- Execução:
--   psql -h localhost -p 5432 -U postgres -d construction -1 -v ON_ERROR_STOP=1 -f este_arquivo.sql
-- =====================================================================

\set ON_ERROR_STOP on
SET client_encoding = 'UTF8';

-- ---------------------------------------------------------------------
-- 0. Controle de versão
-- ---------------------------------------------------------------------
CREATE SCHEMA IF NOT EXISTS migration;

CREATE TABLE IF NOT EXISTS migration.schema_migration (
    version    text PRIMARY KEY,
    applied_at timestamptz NOT NULL DEFAULT now()
);

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM migration.schema_migration
        WHERE version = '2026-10-29-legacy-to-phases'
    ) THEN
        RAISE EXCEPTION 'Migração 2026-10-29-legacy-to-phases já aplicada';
    END IF;
END $$;

-- ---------------------------------------------------------------------
-- 1. Backup lógico dentro do banco
-- ---------------------------------------------------------------------
DO $$
DECLARE
    backup_schema text := 'backup_20261029_pre_migration';
    t record;
BEGIN
    IF EXISTS (SELECT 1 FROM pg_namespace WHERE nspname = backup_schema) THEN
        RAISE EXCEPTION 'Schema de backup % já existe; abortando', backup_schema;
    END IF;

    EXECUTE format('CREATE SCHEMA %I', backup_schema);

    FOR t IN
        SELECT tablename
        FROM pg_tables
        WHERE schemaname = 'public'
        ORDER BY tablename
    LOOP
        EXECUTE format(
            'CREATE TABLE %I.%I AS TABLE public.%I',
            backup_schema, t.tablename, t.tablename
        );
    END LOOP;
END $$;

-- ---------------------------------------------------------------------
-- 2. Integridade do modelo novo
-- ---------------------------------------------------------------------

-- 2.1 Tabelas de junção: remover duplicatas e criar PK composta
DELETE FROM construction_clients a
USING construction_clients b
WHERE a.ctid > b.ctid
  AND a.construction_id = b.construction_id
  AND a.user_id = b.user_id;

ALTER TABLE construction_clients
    ADD CONSTRAINT construction_clients_pkey
    PRIMARY KEY (construction_id, user_id);

DELETE FROM construction_master_builders a
USING construction_master_builders b
WHERE a.ctid > b.ctid
  AND a.construction_id = b.construction_id
  AND a.user_id = b.user_id;

ALTER TABLE construction_master_builders
    ADD CONSTRAINT construction_master_builders_pkey
    PRIMARY KEY (construction_id, user_id);

-- custom_phase_components: sem PK, component_name anulável
DELETE FROM custom_phase_components WHERE component_name IS NULL;

ALTER TABLE custom_phase_components
    ALTER COLUMN component_name SET NOT NULL;

ALTER TABLE custom_phase_components
    ADD CONSTRAINT custom_phase_components_pkey
    PRIMARY KEY (custom_phase_id, component_name);

-- 2.2 FKs obrigatórias
ALTER TABLE phases              ALTER COLUMN construction_id SET NOT NULL;
ALTER TABLE materials           ALTER COLUMN phase_id        SET NOT NULL;
ALTER TABLE tools               ALTER COLUMN phase_id        SET NOT NULL;
ALTER TABLE machinery           ALTER COLUMN phase_id        SET NOT NULL;
ALTER TABLE team_members        ALTER COLUMN phase_id        SET NOT NULL;
ALTER TABLE executed_services   ALTER COLUMN phase_id        SET NOT NULL;
ALTER TABLE photo_records       ALTER COLUMN phase_id        SET NOT NULL;

-- 2.3 Recriar FKs com ON DELETE CASCADE
DO $$
DECLARE
    cname text;
    t text;
BEGIN
    -- phases -> constructions
    SELECT conname INTO cname
    FROM pg_constraint
    WHERE conrelid = 'phases'::regclass
      AND contype = 'f'
      AND confrelid = 'constructions'::regclass
    LIMIT 1;

    IF cname IS NOT NULL THEN
        EXECUTE format('ALTER TABLE phases DROP CONSTRAINT %I', cname);
    END IF;

    ALTER TABLE phases
        ADD CONSTRAINT fk_phases_construction
        FOREIGN KEY (construction_id) REFERENCES constructions(id) ON DELETE CASCADE;

    -- componentes -> phases
    FOREACH t IN ARRAY ARRAY[
        'materials', 'tools', 'machinery', 'team_members',
        'executed_services', 'photo_records'
    ]
    LOOP
        SELECT conname INTO cname
        FROM pg_constraint
        WHERE conrelid = t::regclass
          AND contype = 'f'
          AND confrelid = 'phases'::regclass
        LIMIT 1;

        IF cname IS NOT NULL THEN
            EXECUTE format('ALTER TABLE %I DROP CONSTRAINT %I', t, cname);
        END IF;

        EXECUTE format(
            'ALTER TABLE %I ADD CONSTRAINT %I FOREIGN KEY (phase_id) REFERENCES phases(id) ON DELETE CASCADE',
            t, 'fk_' || t || '_phase'
        );
    END LOOP;
END $$;

-- 2.4 general_information -> phases (antes não havia FK)
ALTER TABLE general_information
    ADD CONSTRAINT fk_general_information_phase
    FOREIGN KEY (phase_id) REFERENCES phases(id) ON DELETE CASCADE;

-- 2.5 Índices para as FKs
CREATE INDEX IF NOT EXISTS idx_phases_construction             ON phases(construction_id);
CREATE INDEX IF NOT EXISTS idx_materials_phase                 ON materials(phase_id);
CREATE INDEX IF NOT EXISTS idx_tools_phase                     ON tools(phase_id);
CREATE INDEX IF NOT EXISTS idx_machinery_phase                 ON machinery(phase_id);
CREATE INDEX IF NOT EXISTS idx_team_members_phase              ON team_members(phase_id);
CREATE INDEX IF NOT EXISTS idx_executed_services_phase         ON executed_services(phase_id);
CREATE INDEX IF NOT EXISTS idx_photo_records_phase             ON photo_records(phase_id);
CREATE INDEX IF NOT EXISTS idx_construction_clients_user       ON construction_clients(user_id);
CREATE INDEX IF NOT EXISTS idx_construction_master_builders_user ON construction_master_builders(user_id);

-- 2.6 CHECKs e consolidação do status
ALTER TABLE executed_services
    DROP CONSTRAINT IF EXISTS executed_services_status_check;

ALTER TABLE executed_services
    ADD CONSTRAINT executed_services_status_check
    CHECK (status IN ('PLANNED', 'STARTED', 'IN_PROGRESS', 'COMPLETED'));

ALTER TABLE executed_services
    ADD CONSTRAINT chk_executed_services_progress
    CHECK (progress BETWEEN 0 AND 100);

ALTER TABLE tools
    ADD CONSTRAINT chk_tools_counts
    CHECK (
        available >= 0
        AND in_use >= 0
        AND in_maintenance >= 0
        AND in_use + in_maintenance <= total_quantity
        AND available = total_quantity - in_use - in_maintenance
    );

ALTER TABLE machinery
    ADD CONSTRAINT chk_machinery_counts
    CHECK (
        in_operation >= 0
        AND in_maintenance >= 0
        AND in_operation + in_maintenance <= total_quantity
    );

-- ---------------------------------------------------------------------
-- 3. Migração dos dados
-- ---------------------------------------------------------------------

CREATE TABLE migration.legacy_phase_map (
    legacy_table   text NOT NULL,
    legacy_id      varchar(255) NOT NULL,
    legacy_name    varchar(255),
    phase_id       varchar(255) NOT NULL,
    construction_id varchar(255) NOT NULL,
    migrated_at    timestamptz NOT NULL DEFAULT now(),
    PRIMARY KEY (legacy_table, legacy_id)
);

CREATE TABLE migration.legacy_component_map (
    legacy_table text NOT NULL,
    legacy_id    varchar(255) NOT NULL,
    new_table    text NOT NULL,
    new_id       varchar(255) NOT NULL,
    phase_id     varchar(255) NOT NULL,
    migrated_at  timestamptz NOT NULL DEFAULT now(),
    PRIMARY KEY (legacy_table, legacy_id)
);

-- 3.1 Um usuário legado -> uma obra nova
CREATE TEMP TABLE mig_users AS
SELECT
    u.id                         AS user_id,
    u.first_name,
    u.last_name,
    u.role,
    gen_random_uuid()::text      AS construction_id
FROM users u
WHERE u.id IN (
    SELECT user_id FROM foundations
    UNION SELECT user_id FROM masonry
    UNION SELECT user_id FROM hydraulics
    UNION SELECT user_id FROM eletric
    UNION SELECT user_id FROM finishings
    UNION SELECT user_id FROM coatings
);

INSERT INTO constructions (id, name, status, owner_id)
SELECT
    m.construction_id,
    'Obra - ' || m.first_name || ' ' || m.last_name,
    'ACTIVE',
    (SELECT id FROM users WHERE role = 'OWNER' ORDER BY id LIMIT 1)
FROM mig_users m;

INSERT INTO construction_clients (construction_id, user_id)
SELECT construction_id, user_id
FROM mig_users
WHERE role = 'CLIENT';

INSERT INTO construction_master_builders (construction_id, user_id)
SELECT construction_id, user_id
FROM mig_users
WHERE role = 'MASTER_BUILDER';

-- 3.2 Cabeçalhos legados -> phases
CREATE TEMP TABLE mig_legacy_phases AS
SELECT 'foundations'::text AS src, id, name, contractor, user_id FROM foundations
UNION ALL
SELECT 'masonry',    id, name, contractor, user_id FROM masonry
UNION ALL
SELECT 'hydraulics', id, name, contractor, user_id FROM hydraulics
UNION ALL
SELECT 'eletric',    id, name, contractor, user_id FROM eletric
UNION ALL
SELECT 'finishings', id, name, contractor, user_id FROM finishings
UNION ALL
SELECT 'coatings',   id, name, contractor, user_id FROM coatings;

INSERT INTO phases (id, name, contractor, construction_id)
SELECT
    l.id,
    CASE l.src
        WHEN 'foundations' THEN 'Fundação'
        WHEN 'masonry'     THEN 'Alvenaria'
        WHEN 'hydraulics'  THEN 'Instalações Hidráulicas'
        WHEN 'eletric'     THEN 'Instalações Elétricas'
        WHEN 'finishings'  THEN 'Acabamentos'
        WHEN 'coatings'    THEN 'Revestimentos'
    END,
    l.contractor,
    m.construction_id
FROM mig_legacy_phases l
JOIN mig_users m ON m.user_id = l.user_id;

INSERT INTO migration.legacy_phase_map
    (legacy_table, legacy_id, legacy_name, phase_id, construction_id)
SELECT l.src, l.id, l.name, l.id, m.construction_id
FROM mig_legacy_phases l
JOIN mig_users m ON m.user_id = l.user_id;

-- 3.3 Componentes (a fundação é o único grupo com dados)
INSERT INTO team_members (id, phase_id, name, role, cpf, hours_worked, status, notes)
SELECT id, foundation_id, name, role, cpf, hours_worked, status, notes
FROM foundation_team_members;

INSERT INTO migration.legacy_component_map
    (legacy_table, legacy_id, new_table, new_id, phase_id)
SELECT 'foundation_team_members', id, 'team_members', id, foundation_id
FROM foundation_team_members;

INSERT INTO executed_services
    (id, phase_id, name, team, planned_hours, executed_hours, status, progress, notes)
SELECT
    id,
    foundation_id,
    name,
    team,
    planned_hours,
    executed_hours,
    CASE status
        WHEN 'CONCLUIDO'    THEN 'COMPLETED'
        WHEN 'EM_ANDAMENTO' THEN 'IN_PROGRESS'
        WHEN 'PLANEJADO'    THEN 'PLANNED'
        WHEN 'INICIADO'     THEN 'STARTED'
        ELSE status
    END,
    least(100, greatest(0, round(executed_hours / planned_hours * 100)))::int,
    notes
FROM foundation_executed_services;

INSERT INTO migration.legacy_component_map
    (legacy_table, legacy_id, new_table, new_id, phase_id)
SELECT 'foundation_executed_services', id, 'executed_services', id, foundation_id
FROM foundation_executed_services;

INSERT INTO machinery
    (id, phase_id, name, category, hours_worked, fuel_used, fuel_unit,
     total_quantity, in_operation, in_maintenance, condition, notes)
SELECT
    id, foundation_id, name, category, hours_worked, fuel_used, fuel_unit,
    total_quantity, in_operation, in_maintenance, condition, notes
FROM foundation_machinery;

INSERT INTO migration.legacy_component_map
    (legacy_table, legacy_id, new_table, new_id, phase_id)
SELECT 'foundation_machinery', id, 'machinery', id, foundation_id
FROM foundation_machinery;

INSERT INTO materials
    (id, phase_id, name, category, consumed_quantity, unit,
     current_stock, minimum_stock, needs_restock, urgency)
SELECT
    id, foundation_id, name, category, consumed_quantity, unit,
    current_stock, minimum_stock, needs_restock, urgency
FROM foundation_materials;

INSERT INTO migration.legacy_component_map
    (legacy_table, legacy_id, new_table, new_id, phase_id)
SELECT 'foundation_materials', id, 'materials', id, foundation_id
FROM foundation_materials;

INSERT INTO tools
    (id, phase_id, name, category, total_quantity, in_use,
     in_maintenance, available, condition, notes)
SELECT
    id, foundation_id, name, category, total_quantity, in_use,
    in_maintenance, total_quantity - in_use - in_maintenance,
    condition, notes
FROM foundation_tools;

INSERT INTO migration.legacy_component_map
    (legacy_table, legacy_id, new_table, new_id, phase_id)
SELECT 'foundation_tools', id, 'tools', id, foundation_id
FROM foundation_tools;

INSERT INTO photo_records (id, phase_id, file_path, caption, category, uploaded_at)
SELECT id, foundation_id, file_path, caption, category, uploaded_at
FROM foundation_photo_records;

INSERT INTO migration.legacy_component_map
    (legacy_table, legacy_id, new_table, new_id, phase_id)
SELECT 'foundation_photo_records', id, 'photo_records', id, foundation_id
FROM foundation_photo_records;

-- ---------------------------------------------------------------------
-- 4. Verificações antes de arquivar as tabelas antigas
-- ---------------------------------------------------------------------
DO $$
DECLARE
    v_expected int;
    v_actual   int;
    v_bad      int;
BEGIN
    SELECT count(*) INTO v_expected FROM mig_legacy_phases;
    SELECT count(*) INTO v_actual   FROM public.phases;
    IF v_actual <> v_expected THEN
        RAISE EXCEPTION 'Fases: esperado %, encontrado %', v_expected, v_actual;
    END IF;

    SELECT count(*) INTO v_expected FROM mig_users;
    SELECT count(*) INTO v_actual
    FROM public.constructions
    WHERE id IN (SELECT construction_id FROM mig_users);
    IF v_actual <> v_expected THEN
        RAISE EXCEPTION 'Obras migradas: esperado %, encontrado %', v_expected, v_actual;
    END IF;

    SELECT count(*) INTO v_expected FROM foundation_team_members;
    SELECT count(*) INTO v_actual   FROM public.team_members;
    IF v_actual <> v_expected THEN
        RAISE EXCEPTION 'Team members: esperado %, encontrado %', v_expected, v_actual;
    END IF;

    SELECT count(*) INTO v_expected FROM foundation_executed_services;
    SELECT count(*) INTO v_actual   FROM public.executed_services;
    IF v_actual <> v_expected THEN
        RAISE EXCEPTION 'Executed services: esperado %, encontrado %', v_expected, v_actual;
    END IF;

    SELECT count(*) INTO v_expected FROM foundation_machinery;
    SELECT count(*) INTO v_actual   FROM public.machinery;
    IF v_actual <> v_expected THEN
        RAISE EXCEPTION 'Machinery: esperado %, encontrado %', v_expected, v_actual;
    END IF;

    SELECT count(*) INTO v_bad
    FROM public.phases p
    LEFT JOIN public.constructions c ON c.id = p.construction_id
    WHERE c.id IS NULL;
    IF v_bad > 0 THEN
        RAISE EXCEPTION 'Fases órfãs: %', v_bad;
    END IF;

    SELECT count(*) INTO v_bad
    FROM public.team_members x
    LEFT JOIN public.phases p ON p.id = x.phase_id
    WHERE p.id IS NULL;
    IF v_bad > 0 THEN
        RAISE EXCEPTION 'Team members órfãos: %', v_bad;
    END IF;

    SELECT count(*) INTO v_bad
    FROM public.executed_services x
    LEFT JOIN public.phases p ON p.id = x.phase_id
    WHERE p.id IS NULL;
    IF v_bad > 0 THEN
        RAISE EXCEPTION 'Executed services órfãos: %', v_bad;
    END IF;

    SELECT count(*) INTO v_bad
    FROM public.machinery x
    LEFT JOIN public.phases p ON p.id = x.phase_id
    WHERE p.id IS NULL;
    IF v_bad > 0 THEN
        RAISE EXCEPTION 'Machinery órfã: %', v_bad;
    END IF;

    SELECT count(*) INTO v_bad
    FROM (
        SELECT construction_id
        FROM public.phases
        WHERE construction_id IN (SELECT construction_id FROM mig_users)
        GROUP BY construction_id
        HAVING count(*) <> 6
    ) x;
    IF v_bad > 0 THEN
        RAISE EXCEPTION 'Obras migradas sem 6 fases: %', v_bad;
    END IF;
END $$;

-- ---------------------------------------------------------------------
-- 5. Arquivar as tabelas antigas no schema legacy
-- ---------------------------------------------------------------------
CREATE SCHEMA legacy;

DO $$
DECLARE
    tables text[] := ARRAY[
        'foundations',
        'foundation_materials',
        'foundation_tools',
        'foundation_machinery',
        'foundation_team_members',
        'foundation_executed_services',
        'foundation_photo_records',
        'masonry',
        'masonry_executed_services',
        'hydraulics',
        'hydraulic_materials',
        'hydraulic_tools',
        'hydraulic_machinery',
        'hydraulic_team_members',
        'hydraulic_executed_services',
        'hydraulic_photo_records',
        'eletric',
        'eletric_materials',
        'eletric_tools',
        'eletric_machineries',
        'eletric_team_members',
        'eletric_executed_services',
        'eletric_photo_records',
        'finishings',
        'finishing_materials',
        'finishing_tools',
        'finishing_machineries',
        'finishing_team_members',
        'finishing_executed_services',
        'finishing_photo_records',
        'coatings',
        'coatings_materials',
        'coatings_tools',
        'coatings_machineries',
        'coatings_team_members',
        'coatings_executed_services',
        'coatings_photo_records',
        'generic_phases',
        'generic_general_info',
        'generic_materials',
        'generic_tools',
        'generic_machinery',
        'generic_team_members',
        'generic_executed_services',
        'generic_photo_records'
    ];
    t text;
BEGIN
    FOREACH t IN ARRAY tables
    LOOP
        EXECUTE format('ALTER TABLE public.%I SET SCHEMA legacy', t);
        EXECUTE format(
            'COMMENT ON TABLE legacy.%I IS %L',
            t,
            'LEGACY - arquivada em 2026-10-29; dados migrados para public.phases e componentes'
        );
    END LOOP;
END $$;

-- ---------------------------------------------------------------------
-- 6. Registrar a migração
-- ---------------------------------------------------------------------
INSERT INTO migration.schema_migration (version)
VALUES ('2026-10-29-legacy-to-phases');
