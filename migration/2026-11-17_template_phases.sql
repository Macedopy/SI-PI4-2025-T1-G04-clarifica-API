-- Fases de cada template (ID estável independente do nome exibido)
CREATE TABLE IF NOT EXISTS template_phases (
    id VARCHAR(36) PRIMARY KEY,
    template_id VARCHAR(36) NOT NULL REFERENCES project_templates(id) ON DELETE CASCADE,
    phase_id VARCHAR(50) NOT NULL,            -- ID estável: 'foundation', 'framing', etc.
    name_key VARCHAR(100) NOT NULL,           -- chave de tradução: 'phase.foundation'
    icon VARCHAR(50),                         -- ícone lucide padrão
    sequence INTEGER NOT NULL,                -- ordem de exibição
    is_optional BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    components JSONB DEFAULT '[]',            -- componentes padrão: ['equipe','servicos',...]
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_template_phases_template ON template_phases(template_id);
CREATE INDEX IF NOT EXISTS idx_template_phases_phase_id ON template_phases(phase_id);
CREATE UNIQUE INDEX IF NOT EXISTS ux_template_phases_template_phase ON template_phases(template_id, phase_id);
