-- Carga de custos da obra Residencial Horizonte (Mestre Geraldo)
-- Gera compras de materiais/ferramentas, maquinário, combustível, manutenção e mão de obra
-- para que o Controle de Custos do Dashboard Geral exiba valores reais.
-- Idempotente: remove os registros de custo das fases desta obra antes de recriar.

BEGIN;

SET client_encoding = 'UTF8';

-- ------------------------------------------------------------ limpeza
DELETE FROM material_purchases mp USING phases p
 WHERE mp.phase_id = p.id AND p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6';

DELETE FROM tool_purchases tp USING phases p
 WHERE tp.phase_id = p.id AND p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6';

DELETE FROM machinery_fuel_logs fl USING phases p
 WHERE fl.phase_id = p.id AND p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6';

DELETE FROM machinery_maintenance_logs ml USING phases p
 WHERE ml.phase_id = p.id AND p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6';

DELETE FROM team_member_work_logs wl USING phases p
 WHERE wl.phase_id = p.id AND p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6';

DELETE FROM machinery_records mr USING phases p
 WHERE mr.phase_id = p.id AND p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6';

-- ---------------------------------------------------- compras de material
WITH ranked AS (
    SELECT m.id, m.phase_id, m.category,
           row_number() OVER (PARTITION BY m.phase_id ORDER BY m.name) AS rn
    FROM materials m
    JOIN phases p ON p.id = m.phase_id
    WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6'
)
INSERT INTO material_purchases
    (id, material_id, phase_id, quantity, unit_price, total_price,
     purchase_date, purchased_by, supplier, notes, created_at)
SELECT gen_random_uuid()::text,
       r.id,
       r.phase_id,
       q.qty,
       pr.price,
       q.qty * pr.price,
       CURRENT_DATE - (r.rn * 12)::int,
       '1ff4aecb-a40b-4039-806e-485dc63f5da5',
       CASE WHEN r.rn = 1 THEN 'Depósito Central' ELSE 'Materiais Silva' END,
       'Compra de material para a fase',
       NOW() - (((r.rn * 4)::int) || ' days')::interval
FROM ranked r
JOIN (VALUES (1, 20.0), (2, 12.0)) AS q(rn, qty) ON q.rn = r.rn
CROSS JOIN LATERAL (
    SELECT CASE r.category
               WHEN 'CEMENT'     THEN 38.50
               WHEN 'SAND'       THEN 95.00
               WHEN 'STEEL'      THEN 12.40
               WHEN 'BRICK'      THEN 1.80
               WHEN 'WOOD'       THEN 45.00
               WHEN 'ELECTRICAL' THEN 3.20
               WHEN 'PLUMBING'   THEN 8.50
               ELSE 25.00
           END AS price
) pr
WHERE r.rn <= 2;

-- ------------------------------------------------ compras de ferramentas
WITH ranked AS (
    SELECT t.id, t.phase_id,
           row_number() OVER (PARTITION BY t.phase_id ORDER BY t.name) AS rn
    FROM tools t
    JOIN phases p ON p.id = t.phase_id
    WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6'
)
INSERT INTO tool_purchases
    (id, tool_id, phase_id, quantity, unit_price, total_price,
     purchase_date, purchased_by, supplier, notes, created_at)
SELECT gen_random_uuid()::text,
       r.id,
       r.phase_id,
       1,
       320.00,
       320.00,
       CURRENT_DATE - 18,
       '1ff4aecb-a40b-4039-806e-485dc63f5da5',
       'Ferramentas Alfa',
       'Aquisição de ferramenta',
       NOW() - interval '18 days'
FROM ranked r
WHERE r.rn = 1;

-- --------------------------------------------------- maquinário (registro)
WITH ranked AS (
    SELECT mc.id, mc.phase_id,
           row_number() OVER (PARTITION BY mc.phase_id ORDER BY mc.name) AS rn
    FROM machinery mc
    JOIN phases p ON p.id = mc.phase_id
    WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6'
)
INSERT INTO machinery_records
    (id, machinery_id, phase_id, is_owned, rental_type, rental_price,
     status, current_value, total_fuel_cost, total_maintenance_cost, created_at, updated_at)
SELECT gen_random_uuid()::text,
       r.id,
       r.phase_id,
       (r.rn = 2),
       CASE WHEN r.rn = 1 THEN 'diária' ELSE NULL END,
       CASE WHEN r.rn = 1 THEN 1200.00 ELSE 0 END,
       'active',
       CASE WHEN r.rn = 1 THEN 45000.00 ELSE 78000.00 END,
       0,
       0,
       NOW(),
       NOW()
FROM ranked r
WHERE r.rn <= 2;

-- ---------------------------------------------------------- combustível
WITH ranked AS (
    SELECT mc.id, mc.phase_id, mc.fuel_unit,
           row_number() OVER (PARTITION BY mc.phase_id ORDER BY mc.name) AS rn
    FROM machinery mc
    JOIN phases p ON p.id = mc.phase_id
    WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6'
)
INSERT INTO machinery_fuel_logs
    (id, machinery_id, phase_id, log_date, liters, price_per_liter,
     total_cost, recorded_by, notes, created_at)
SELECT gen_random_uuid()::text,
       r.id,
       r.phase_id,
       CURRENT_DATE - d.days,
       d.liters,
       6.20,
       d.liters * 6.20,
       '1ff4aecb-a40b-4039-806e-485dc63f5da5',
       'Abastecimento',
       NOW() - ((d.days) || ' days')::interval
FROM ranked r
CROSS JOIN (VALUES (4, 80.0), (11, 60.0)) AS d(days, liters)
WHERE r.rn = 1
  AND r.fuel_unit = 'LITERS';

-- ---------------------------------------------------------- manutenção
WITH ranked AS (
    SELECT mc.id, mc.phase_id,
           row_number() OVER (PARTITION BY mc.phase_id ORDER BY mc.name) AS rn
    FROM machinery mc
    JOIN phases p ON p.id = mc.phase_id
    WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6'
)
INSERT INTO machinery_maintenance_logs
    (id, machinery_id, phase_id, maintenance_date, description, cost,
     performed_by, notes, created_at)
SELECT gen_random_uuid()::text,
       r.id,
       r.phase_id,
       CURRENT_DATE - 8,
       'Revisão preventiva e troca de óleo',
       480.00,
       '1ff4aecb-a40b-4039-806e-485dc63f5da5',
       'Serviço executado na oficina credenciada',
       NOW() - interval '8 days'
FROM ranked r
WHERE r.rn = 1;

-- ---------------------------------------------------------- mão de obra
WITH base AS (
    SELECT tm.id, tm.phase_id, tm.role,
           CASE tm.role
               WHEN 'Mestre de obras'         THEN 75.0
               WHEN 'Encarregado'             THEN 55.0
               WHEN 'Encarregado elétrico'    THEN 58.0
               WHEN 'Eletricista'             THEN 48.0
               WHEN 'Encanador'               THEN 46.0
               WHEN 'Armador'                 THEN 42.0
               WHEN 'Carpinteiro'             THEN 42.0
               WHEN 'Carpinteiro de formas'   THEN 43.0
               WHEN 'Pedreiro'                THEN 38.0
               WHEN 'Pintor'                  THEN 36.0
               WHEN 'Pintora'                 THEN 36.0
               WHEN 'Marceneiro'              THEN 44.0
               WHEN 'Operador de máquina'     THEN 50.0
               WHEN 'Sinaleiro'               THEN 30.0
               WHEN 'Servente'                THEN 25.0
               WHEN 'Auxiliar de instalação'  THEN 32.0
               WHEN 'Auxiliar de limpeza'     THEN 24.0
               ELSE 35.0
           END AS rate
    FROM team_members tm
    JOIN phases p ON p.id = tm.phase_id
    WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6'
)
INSERT INTO team_member_work_logs
    (id, team_member_id, phase_id, work_date, hours_worked, hourly_rate,
     total_cost, added_by, notes, created_at)
SELECT gen_random_uuid()::text,
       b.id,
       b.phase_id,
       CURRENT_DATE - d.days,
       8,
       b.rate,
       8 * b.rate,
       '1ff4aecb-a40b-4039-806e-485dc63f5da5',
       '',
       NOW() - ((d.days) || ' days')::interval
FROM base b
CROSS JOIN (VALUES (2), (9), (16), (23)) AS d(days);

COMMIT;

-- Verificação
SELECT p.name AS fase,
       (SELECT COALESCE(sum(mp.total_price), 0) FROM material_purchases mp WHERE mp.phase_id = p.id) AS materiais,
       (SELECT COALESCE(sum(tp.total_price), 0) FROM tool_purchases tp WHERE tp.phase_id = p.id) AS ferramentas,
       (SELECT COALESCE(sum(mr.rental_price), 0) FROM machinery_records mr WHERE mr.phase_id = p.id AND mr.is_owned = false) AS aluguel,
       (SELECT COALESCE(sum(fl.total_cost), 0) FROM machinery_fuel_logs fl WHERE fl.phase_id = p.id) AS combustivel,
       (SELECT COALESCE(sum(ml.cost), 0) FROM machinery_maintenance_logs ml WHERE ml.phase_id = p.id) AS manutencao,
       (SELECT COALESCE(sum(wl.total_cost), 0) FROM team_member_work_logs wl WHERE wl.phase_id = p.id) AS mao_de_obra
FROM phases p
WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6'
ORDER BY p.name;
