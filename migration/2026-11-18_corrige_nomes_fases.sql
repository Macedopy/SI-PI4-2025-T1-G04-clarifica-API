-- Corrige nomes de fases existentes que estao nulos ou sao UUIDs brutos
-- Fases com nome nulo ficam como "Fase <primeiros 8 chars do ID>"
UPDATE phases SET name = 'Fase ' || substring(id from 1 for 8)
WHERE name IS NULL OR name = '' OR name LIKE 'phase.%' OR name ~ '^[0-9a-f]{8}-';

-- Garante que contractor nao seja nulo para fases antigas
UPDATE phases SET contractor = 'Responsável não definido'
WHERE contractor IS NULL OR contractor = '';
