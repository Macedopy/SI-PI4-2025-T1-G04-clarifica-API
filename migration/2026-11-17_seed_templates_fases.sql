-- Seed: Templates de fases (Brasil e EUA/Canadá) baseado no documento residential_construction_lifecycle

-- Template Brasil (fases atuais do app)
INSERT INTO project_templates (id, country_code, name, description, is_default)
VALUES ('11111111-1111-1111-1111-111111111111', 'BR', 'Residencial Brasil', 'Fases padrão para construção residencial no Brasil', true)
ON CONFLICT (id) DO NOTHING;

-- Template EUA/Canadá (conforme documento residential_construction_lifecycle)
INSERT INTO project_templates (id, country_code, name, description, is_default)
VALUES ('22222222-2222-2222-2222-222222222222', 'US', 'Residencial EUA / Canadá', '11 fases conforme modelo residential_construction_lifecycle (EUA e Canadá)', false)
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- Fases do Template Brasil (IDs estáveis, nomes traduzidos via JSON)
-- ============================================================
INSERT INTO template_phases (template_id, phase_id, name_key, icon, sequence, is_optional, is_active, components)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'preparacao_terreno', 'phase.preparacao_terreno', 'trees', 1, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('11111111-1111-1111-1111-111111111111', 'fundacao', 'phase.fundacao', 'layers', 2, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('11111111-1111-1111-1111-111111111111', 'estrutura', 'phase.estrutura', 'building-2', 3, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('11111111-1111-1111-1111-111111111111', 'alvenaria', 'phase.alvenaria', 'brick-wall', 4, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('11111111-1111-1111-1111-111111111111', 'instalacoes_eletricas', 'phase.instalacoes_eletricas', 'zap', 5, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('11111111-1111-1111-1111-111111111111', 'instalacoes_hidraulicas', 'phase.instalacoes_hidraulicas', 'droplets', 6, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('11111111-1111-1111-1111-111111111111', 'revestimentos', 'phase.revestimentos', 'ruler', 7, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('11111111-1111-1111-1111-111111111111', 'acabamentos', 'phase.acabamentos', 'paint-bucket', 8, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]')
ON CONFLICT (template_id, phase_id) DO NOTHING;

-- ============================================================
-- Fases do Template EUA/Canadá (conforme documento)
-- ============================================================
INSERT INTO template_phases (template_id, phase_id, name_key, icon, sequence, is_optional, is_active, components)
VALUES
  ('22222222-2222-2222-2222-222222222222', 'pre_construction', 'phase.pre_construction', 'home', 1, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('22222222-2222-2222-2222-222222222222', 'site_preparation', 'phase.site_preparation', 'shovel', 2, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('22222222-2222-2222-2222-222222222222', 'foundation', 'phase.foundation', 'layers', 3, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('22222222-2222-2222-2222-222222222222', 'framing', 'phase.framing', 'hard-hat', 4, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('22222222-2222-2222-2222-222222222222', 'building_envelope', 'phase.building_envelope', 'roof', 5, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('22222222-2222-2222-2222-222222222222', 'mep_rough_in', 'phase.mep_rough_in', 'zap', 6, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('22222222-2222-2222-2222-222222222222', 'insulation_drywall', 'phase.insulation_drywall', 'droplets', 7, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('22222222-2222-2222-2222-222222222222', 'interior_finishes', 'phase.interior_finishes', 'paint-bucket', 8, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('22222222-2222-2222-2222-222222222222', 'exterior_finishes', 'phase.exterior_finishes', 'brush', 9, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('22222222-2222-2222-2222-222222222222', 'final_systems_inspections', 'phase.final_systems_inspections', 'alert-triangle', 10, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]'),
  ('22222222-2222-2222-2222-222222222222', 'closeout_handover', 'phase.closeout_handover', 'package', 11, false, true, '["equipe","servicos","maquinarios","materiais","ferramentas","fotos"]')
ON CONFLICT (template_id, phase_id) DO NOTHING;
