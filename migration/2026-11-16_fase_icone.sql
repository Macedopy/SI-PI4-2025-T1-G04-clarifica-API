-- Ícone personalizado por fase (selecionado na criação da fase).
ALTER TABLE phases ADD COLUMN IF NOT EXISTS icon VARCHAR(50);

-- Ícones para as fases já existentes (mantém o visual coerente).
UPDATE phases SET icon = 'trees' WHERE icon IS NULL AND name = 'Preparação do Terreno';
UPDATE phases SET icon = 'layers' WHERE icon IS NULL AND name = 'Fundação';
UPDATE phases SET icon = 'building-2' WHERE icon IS NULL AND name = 'Estrutura';
UPDATE phases SET icon = 'brick-wall' WHERE icon IS NULL AND name = 'Alvenaria';
UPDATE phases SET icon = 'zap' WHERE icon IS NULL AND name = 'Instalações Elétricas';
UPDATE phases SET icon = 'droplets' WHERE icon IS NULL AND name = 'Instalações Hidráulicas';
UPDATE phases SET icon = 'ruler' WHERE icon IS NULL AND name = 'Revestimentos';
UPDATE phases SET icon = 'paint-bucket' WHERE icon IS NULL AND name = 'Acabamentos';
UPDATE phases SET icon = 'hammer' WHERE icon IS NULL AND name = 'Pintura';
UPDATE phases SET icon = 'home' WHERE icon IS NULL AND name = 'Telhado';
